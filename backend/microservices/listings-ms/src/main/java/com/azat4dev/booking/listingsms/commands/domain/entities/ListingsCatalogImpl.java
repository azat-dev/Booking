package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.events.AddedNewPhotoToListing;
import com.azat4dev.booking.listingsms.commands.domain.events.ListingDetailsUpdated;
import com.azat4dev.booking.listingsms.commands.domain.events.NewListingAdded;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.interfaces.tracing.ExtractTraceContext;
import com.azat4dev.booking.shared.utils.TimeProvider;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Observed
@AllArgsConstructor
public class ListingsCatalogImpl implements ListingsCatalog {

    private final UnitOfWorkFactory unitOfWorkFactory;
    private final MarkOutboxNeedsSynchronization markOutboxNeedsSynchronization;
    private final TimeProvider timeProvider;
    private final ExtractTraceContext extractTraceContext;

    @Override
    public void addNew(
        ListingId listingId,
        HostId hostId,
        ListingTitle title
    ) {
        final var now = timeProvider.currentTime();

        final var newListing = Listing.makeNewDraft(
            listingId,
            now,
            hostId,
            title
        );

        final var unitOfWork = unitOfWorkFactory.make();

        try {
            final var listings = unitOfWork.getListingsRepository();
            final var outbox = unitOfWork.getOutboxEventsRepository();

            listings.addNew(newListing);

            outbox.publish(
                new NewListingAdded(
                    listingId,
                    hostId,
                    title
                ),
                extractTraceContext.execute()
            );

            unitOfWork.save();

            log.atInfo()
                .addKeyValue("listingId", listingId::getValue)
                .log("New listing added");
        } catch (Throwable e) {
            unitOfWork.rollback();
            log.atError()
                .setCause(e)
                .log("Failed to add new listing");
            throw e;
        } finally {
            markOutboxNeedsSynchronization.execute();
        }
    }

    private List<DomainEventPayload> getEventsAfterUpdate(Listing prevListingState, Listing newListing) {

        final var prevPhotosIds = prevListingState.getPhotos()
            .stream()
            .map(ListingPhoto::getId)
            .collect(Collectors.toSet());

        final var addedPhotos = newListing.getPhotos()
            .stream()
            .filter(p -> !prevPhotosIds.contains(p.getId()))
            .toList();

        final List<DomainEventPayload> events = new LinkedList<>();

        addedPhotos.forEach(newPhoto -> events.add(
            new AddedNewPhotoToListing(
                newListing.getId(),
                newPhoto
            )
        ));

        final var newValues = ListingDetailsUpdated.Change.from(prevListingState, newListing);
        final var prevValues = ListingDetailsUpdated.Change.from(newListing, prevListingState);

        if (!newValues.equals(prevValues)) {
            events.add(
                new ListingDetailsUpdated(
                    newListing.getId(),
                    newListing.getUpdatedAt(),
                    prevValues,
                    newValues
                )
            );
        }

        return events;
    }

    @Override
    public void update(Listing listing) throws Exception.ListingNotFound {

        final var unitOfWork = unitOfWorkFactory.make();

        try {

            final var listingsRepository = unitOfWork.getListingsRepository();
            final var outbox = unitOfWork.getOutboxEventsRepository();

            final var prevListingState = listingsRepository.findById(listing.getId())
                .orElseThrow(Exception.ListingNotFound::new);

            listingsRepository.update(listing);

            final var events = getEventsAfterUpdate(prevListingState, listing);

            final var traceInfo = extractTraceContext.execute();
            events.forEach(event -> outbox.publish(event, traceInfo));

            unitOfWork.save();
        } catch (ListingsRepository.Exception.ListingNotFound e) {
            unitOfWork.rollback();
            throw new Exception.ListingNotFound();
        } finally {
            markOutboxNeedsSynchronization.execute();
        }
    }

    @FunctionalInterface
    public interface MarkOutboxNeedsSynchronization {
        void execute();
    }
}
