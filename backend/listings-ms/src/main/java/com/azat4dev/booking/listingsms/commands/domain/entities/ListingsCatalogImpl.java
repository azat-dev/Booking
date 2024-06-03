package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.events.AddedNewPhotoToListing;
import com.azat4dev.booking.listingsms.commands.domain.events.NewListingAdded;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ListingsCatalogImpl implements ListingsCatalog {

    private final UnitOfWorkFactory unitOfWorkFactory;
    private final TimeProvider timeProvider;

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
                )
            );

            unitOfWork.save();
        } catch (Throwable e) {
            unitOfWork.rollback();
            throw e;
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
            events.forEach(outbox::publish);

            unitOfWork.save();
        } catch (ListingsRepository.Exception.ListingNotFound e) {
            unitOfWork.rollback();
            throw new Exception.ListingNotFound();
        }
    }
}
