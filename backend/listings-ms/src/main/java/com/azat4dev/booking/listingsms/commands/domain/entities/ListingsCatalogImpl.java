package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.events.AddedNewPhotoToListing;
import com.azat4dev.booking.listingsms.commands.domain.events.ListingUpdated;
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
import java.util.Optional;
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

    private Optional<StateDifference> getStateDifference(Listing prevListingState, Listing newListing) {
        var newStateBuilder = ListingUpdated.State.builder();
        var prevStateBuilder = ListingUpdated.State.builder();

        if (!prevListingState.getTitle().equals(newListing.getTitle())) {
            newStateBuilder.title(newListing.getTitle());
            prevStateBuilder.title(prevListingState.getTitle());
        }

        if (!prevListingState.getStatus().equals(newListing.getStatus())) {
            newStateBuilder.status(newListing.getStatus());
            prevStateBuilder.status(prevListingState.getStatus());
        }

        if (!prevListingState.getDescription().equals(newListing.getDescription())) {
            newStateBuilder.description(newListing.getDescription());
            prevStateBuilder.description(prevListingState.getDescription());
        }

        if (!prevListingState.getPropertyType().equals(newListing.getPropertyType())) {
            newStateBuilder.propertyType(newListing.getPropertyType());
            prevStateBuilder.propertyType(prevListingState.getPropertyType());
        }

        if (!prevListingState.getRoomType().equals(newListing.getRoomType())) {
            newStateBuilder.roomType(newListing.getRoomType());
            prevStateBuilder.roomType(prevListingState.getRoomType());
        }

        if (!prevListingState.getAddress().equals(newListing.getAddress())) {
            newStateBuilder.address(newListing.getAddress());
            prevStateBuilder.address(prevListingState.getAddress());
        }

        if (!prevListingState.getGuestsCapacity().equals(newListing.getGuestsCapacity())) {
            newStateBuilder.guestsCapacity(newListing.getGuestsCapacity());
            prevStateBuilder.guestsCapacity(prevListingState.getGuestsCapacity());
        }

        final var newState = newStateBuilder.build();
        final var prevState = prevStateBuilder.build();

        if (newState.equals(prevState)) {
            return Optional.empty();
        }
        return Optional.of(new StateDifference(prevState, newState));
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

        final var stateDifference = getStateDifference(prevListingState, newListing);

        stateDifference.ifPresent(
            diff -> events.add(
                new ListingUpdated(
                    newListing.getId(),
                    newListing.getUpdatedAt(),
                    diff.prevState(),
                    diff.newState()
                )
            )
        );

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

    private record StateDifference(
        ListingUpdated.State prevState,
        ListingUpdated.State newState
    ) {
    }
}
