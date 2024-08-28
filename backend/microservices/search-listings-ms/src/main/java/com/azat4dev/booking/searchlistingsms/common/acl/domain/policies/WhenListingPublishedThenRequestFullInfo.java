package com.azat4dev.booking.searchlistingsms.common.acl.domain.policies;

import com.azat4dev.booking.searchlistingsms.common.acl.domain.events.IncomingListingPublished;
import com.azat4dev.booking.searchlistingsms.common.acl.domain.events.output.GetListingPublicDetailsById;
import com.azat4dev.booking.searchlistingsms.common.acl.domain.events.output.WaitingInfoForPublishedListing;
import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Observed
@AllArgsConstructor
public class WhenListingPublishedThenRequestFullInfo implements Policy<IncomingListingPublished> {

    private final DomainEventsBus bus;

    @Override
    public void execute(IncomingListingPublished event, EventId eventId, LocalDateTime issuedAt) {

        bus.publish(
            new GetListingPublicDetailsById(event.listingId())
        );

        bus.publish(
            new WaitingInfoForPublishedListing(
                event.listingId(),
                event.publishedAt()
            )
        );
    }

    @Override
    public Class<IncomingListingPublished> getEventClass() {
        return IncomingListingPublished.class;
    }
}
