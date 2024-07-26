package com.azat4dev.booking.listingsms.commands.infrastructure.api.bus;

import com.azat4dev.booking.listingsms.generated.api.bus.QueriesGetPublicListingDetailsByIdEndpoint;
import com.azat4dev.booking.listingsms.generated.events.dto.GetPublicListingDetailsByIdDTO;
import com.azat4dev.booking.listingsms.queries.application.handlers.GetPublicListingDetailsHandler;
import com.azat4dev.booking.shared.infrastructure.api.bus.InputMessage;
import com.azat4dev.booking.shared.infrastructure.api.bus.MessageBusEndpoint;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


//@Component
@AllArgsConstructor
public class QueriesGetPublicListingDetailsByIdApi implements QueriesGetPublicListingDetailsByIdEndpoint {

    private final MessageBus<String> messageBus;
//    private final GetPublicListingDetailsHandler handler;

    @Override
    public void handle(InputMessage<GetPublicListingDetailsByIdDTO> message) {

//        messageBus.publish(replyAddress(), new GetPublicListingDetailsByIdResponseDTO();
    }

    @Override
    public Class<GetPublicListingDetailsByIdDTO> getInputMessageType() {
        return null;
    }
}
