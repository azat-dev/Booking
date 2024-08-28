package com.azat4dev.booking.searchlistingsms.common.acl.domain.policies;

import com.azat4dev.booking.searchlistingsms.commands.domain.events.incoming.ListingPublished;
import com.azat4dev.booking.searchlistingsms.common.acl.domain.events.ReceivedListingInfoForPublishedListing;
import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Observed
@AllArgsConstructor
public class WhenReceivedPublishedListingInfoThenEnrichDataAndPublish implements Policy<ReceivedListingInfoForPublishedListing> {

    private final DomainEventsBus bus;

    @Override
    public void execute(ReceivedListingInfoForPublishedListing event, EventId eventId, LocalDateTime issuedAt) {

        final var input = event.inputEvent();

        bus.publish(
            new ListingPublished(
                input.listingId(),
                input.publishedAt(),
                event.listingInfo()
            )
        );
    }

    @Override
    public Class<ReceivedListingInfoForPublishedListing> getEventClass() {
        return ReceivedListingInfoForPublishedListing.class;
    }
}
