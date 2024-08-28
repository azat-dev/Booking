package com.azat4dev.booking.searchlistingsms.commands.domain.policies;

import com.azat4dev.booking.searchlistingsms.commands.domain.events.FailedToAddListingToSearch;
import com.azat4dev.booking.searchlistingsms.commands.domain.events.ListingAddedToSearch;
import com.azat4dev.booking.searchlistingsms.commands.domain.events.incoming.ListingPublished;
import com.azat4dev.booking.searchlistingsms.commands.domain.interfaces.ListingsDetailsService;
import com.azat4dev.booking.searchlistingsms.commands.domain.interfaces.ListingsSearchRepository;
import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class AddListingToSearchingAfterPublishingPolicy implements Policy<ListingPublished> {

    private final ListingsDetailsService listingsDetailsService;
    private final ListingsSearchRepository listingsSearchRepository;
    private final DomainEventsBus bus;

    @Override
    public void execute(ListingPublished event, EventId eventId, LocalDateTime localDateTime) {

        final var listingId = event.listingId();

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

    @Override
    public Class<ListingPublished> getEventClass() {
        return ListingPublished.class;
    }
}
