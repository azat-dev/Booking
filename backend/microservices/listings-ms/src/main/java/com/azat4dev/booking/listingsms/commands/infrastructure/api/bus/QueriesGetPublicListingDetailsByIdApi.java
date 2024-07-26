package com.azat4dev.booking.listingsms.commands.infrastructure.api.bus;

import com.azat4dev.booking.listingsms.generated.api.bus.QueriesGetPublicListingDetailsByIdEndpoint;
import com.azat4dev.booking.listingsms.generated.events.dto.GetPublicListingDetailsByIdDTO;
import com.azat4dev.booking.listingsms.generated.events.dto.GetPublicListingDetailsByIdResponseDTO;
import com.azat4dev.booking.listingsms.generated.events.dto.PublicListingDetailsDTO;
import com.azat4dev.booking.shared.infrastructure.api.bus.InputMessage;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Observed
@Component
@RequiredArgsConstructor
public class QueriesGetPublicListingDetailsByIdApi implements QueriesGetPublicListingDetailsByIdEndpoint {

    @Override
    public void handle(
        InputMessage<GetPublicListingDetailsByIdDTO> request,
        MessageBus<String> messageBus
    ) {

        final var replyAddress = getReplyAddress().get();
        messageBus.publish(
            replyAddress,
            Optional.empty(),
            Optional.of(request.id()),
            UUID.randomUUID().toString(),
            "GetPublicListingDetailsByIdResponse",
            GetPublicListingDetailsByIdResponseDTO.builder()
                .correlationId(request.id())
                .params(request.message().getParams())
                .data(
                    PublicListingDetailsDTO.builder()
                        .listingId(UUID.randomUUID())
                        .title("Title")
                        .build()
                ).build()
        );
    }
}
