package com.azat4dev.booking.listingsms.commands.infrastructure.api.bus;

import com.azat4dev.booking.listingsms.generated.api.bus.QueriesGetListingPublicDetailsByIdEndpoint;
import com.azat4dev.booking.listingsms.generated.events.dto.*;
import com.azat4dev.booking.listingsms.queries.application.commands.GetListingPublicDetails;
import com.azat4dev.booking.listingsms.queries.application.handlers.GetListingPublicDetailsHandler;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPublicDetails;
import com.azat4dev.booking.shared.infrastructure.api.bus.Request;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Observed
@Component
@RequiredArgsConstructor
public class QueriesGetListingPublicDetailsByIdApi implements QueriesGetListingPublicDetailsByIdEndpoint {

    private final Serializer<ListingPublicDetails, ListingPublicDetailsDTO> mapListingPublicDetails;
    private final GetListingPublicDetailsHandler getListingPublicDetailsHandler;

    @Override
    public void handle(Request<GetListingPublicDetailsByIdDTO> request, Reply reply) {

        final var message = request.message();

        try {
            final var result = getListingPublicDetailsHandler.handle(
                new GetListingPublicDetails(
                    request.message().getParams().getListingId().toString()
                )
            );

            reply.publish(
                new GetListingPublicDetailsByIdResponseDTO(
                    message.getParams(),
                    mapListingPublicDetails.serialize(result)
                )
            );

        } catch (GetListingPublicDetailsHandler.Exception.ListingNotFound e) {
            reply.publish(
                new FailedGetListingPublicDetailsByIdDTO(
                    message.getParams(),
                    new ErrorDTO(
                        ErrorCodeDTO.NOT_FOUND,
                        "Listing not found"
                    )
                )
            );

        } catch (GetListingPublicDetailsHandler.Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleException(
        Throwable exception,
        Request<GetListingPublicDetailsByIdDTO> request,
        Reply reply
    ) {
        final var message = request.message();

        reply.publish(
            new FailedGetListingPublicDetailsByIdDTO(
                message.getParams(),
                new ErrorDTO(
                    ErrorCodeDTO.INTERNAL_SERVER_ERROR,
                    ErrorCodeDTO.INTERNAL_SERVER_ERROR.getValue()
                )
            )
        );
    }
}
