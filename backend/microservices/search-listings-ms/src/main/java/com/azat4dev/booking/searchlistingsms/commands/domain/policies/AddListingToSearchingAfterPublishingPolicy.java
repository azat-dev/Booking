package com.azat4dev.booking.searchlistingsms.commands.domain.policies;

import com.azat4dev.booking.searchlistingsms.commands.domain.events.FailedToAddListingToSearch;
import com.azat4dev.booking.searchlistingsms.commands.domain.events.ListingAddedToSearch;
import com.azat4dev.booking.searchlistingsms.commands.domain.events.incoming.ListingPublished;
import com.azat4dev.booking.searchlistingsms.commands.domain.interfaces.ListingsDetailsService;
import com.azat4dev.booking.searchlistingsms.commands.domain.interfaces.ListingsSearchRepository;
import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddListingToSearchingAfterPublishingPolicy implements Policy<DomainEvent<ListingPublished>> {

    private final ListingsDetailsService listingsDetailsService;
    private final ListingsSearchRepository listingsSearchRepository;
    private final DomainEventsBus bus;

    @Override
    public void execute(DomainEvent<ListingPublished> event) {

        final var payload = event.payload();
        final var listingId = payload.listingId();

        try {
            final var details = listingsDetailsService.getDetailsFor(listingId);
            listingsSearchRepository.addNew(
                listingId,
                details.title(),
                details.description(),
                details.photos()
            );
            bus.publish(new ListingAddedToSearch(listingId));
        } catch (Exception e) {
            this.bus.publish(new FailedToAddListingToSearch(
                listingId
            ));
        }
    }
}
