package com.azat4dev.booking.listingsms.commands.infrastructure.api.bus;

import com.azat4dev.booking.listingsms.generated.api.bus.QueriesGetPublicListingDetailsByIdEndpoint;
import com.azat4dev.booking.listingsms.generated.events.dto.ErrorCodeDTO;
import com.azat4dev.booking.listingsms.generated.events.dto.ErrorDTO;
import com.azat4dev.booking.listingsms.generated.events.dto.FailedGetPublicListingDetailsByIdDTO;
import com.azat4dev.booking.listingsms.generated.events.dto.GetPublicListingDetailsByIdDTO;
import com.azat4dev.booking.listingsms.queries.application.commands.GetPublicListingDetails;
import com.azat4dev.booking.listingsms.queries.application.handlers.GetPublicListingDetailsHandler;
import com.azat4dev.booking.shared.domain.events.EventIdGenerator;
import com.azat4dev.booking.shared.infrastructure.api.bus.InputMessage;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Observed
@Component
@RequiredArgsConstructor
public class QueriesGetPublicListingDetailsByIdApi implements QueriesGetPublicListingDetailsByIdEndpoint {

    private final GetPublicListingDetailsHandler getPublicListingDetailsHandler;
    private final EventIdGenerator eventIdGenerator;

    @Override
    public void handle(
        InputMessage<GetPublicListingDetailsByIdDTO> request,
        MessageBus<String> messageBus
    ) {

        final var message = request.message();

        try {
            final var result = getPublicListingDetailsHandler.handle(
                new GetPublicListingDetails(
                    request.message().getParams().getListingId().toString()
                )
            );
        } catch (GetPublicListingDetailsHandler.Exception.ListingNotFound e) {
            messageBus.publish(
                getReplyAddress().get(),
                Optional.of(message.getParams().getListingId().toString()),
                Optional.of(request.id()),
                eventIdGenerator.generate().getValue(),
                "FailedGetPublicListingDetailsById",
                FailedGetPublicListingDetailsByIdDTO.builder()
                    .params(message.getParams())
                    .error(
                        ErrorDTO.builder()
                            .code(ErrorCodeDTO.NOT_FOUND)
                            .message("Listing not found")
                            .build()
                    ).build()
            );
        } catch (GetPublicListingDetailsHandler.Exception e) {
            throw new RuntimeException(e);
        }
    }
}
