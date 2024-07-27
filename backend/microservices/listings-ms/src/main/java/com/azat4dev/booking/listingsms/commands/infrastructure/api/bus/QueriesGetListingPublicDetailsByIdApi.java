package com.azat4dev.booking.listingsms.commands.infrastructure.api.bus;

import com.azat4dev.booking.listingsms.generated.api.bus.QueriesGetListingPublicDetailsByIdEndpoint;
import com.azat4dev.booking.listingsms.generated.events.dto.*;
import com.azat4dev.booking.listingsms.queries.application.commands.GetListingPublicDetails;
import com.azat4dev.booking.listingsms.queries.application.handlers.GetListingPublicDetailsHandler;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPublicDetails;
import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.infrastructure.api.bus.Request;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Observed
@Component
@RequiredArgsConstructor
public class QueriesGetListingPublicDetailsByIdApi implements QueriesGetListingPublicDetailsByIdEndpoint {

    private static final Logger log = LoggerFactory.getLogger(QueriesGetListingPublicDetailsByIdApi.class);
    private final Serializer<ListingPublicDetails, ListingPublicDetailsDTO> mapListingPublicDetails;
    private final GetListingPublicDetailsHandler getListingPublicDetailsHandler;

    @Override
    public void handle(Request<GetListingPublicDetailsByIdDTO> request, Reply reply) throws Exception {

        final var message = request.message();

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
    }

    @Override
    public void handleException(
        Throwable exception,
        Request<GetListingPublicDetailsByIdDTO> request,
        Reply reply
    ) {

        final var message = request.message();
        var code = FailedGetListingPublicDetailsByIdErrorCodeDTO.INTERNAL_SERVER_ERROR;
        var messageText = exception.getMessage();

        switch (exception) {

            case GetListingPublicDetailsHandler.Exception.ListingNotFound inst:
                code = FailedGetListingPublicDetailsByIdErrorCodeDTO.NOT_FOUND;
                messageText = inst.getMessage();
                break;

            case GetListingPublicDetailsHandler.Exception.Forbidden inst:
                code = FailedGetListingPublicDetailsByIdErrorCodeDTO.FORBIDDEN;
                messageText = inst.getMessage();
                break;

            case ValidationException inst:
                code = FailedGetListingPublicDetailsByIdErrorCodeDTO.INVALID_PARAMETERS;
                messageText = inst.getMessage();
                break;
            default:
                log.atError()
                    .setCause(exception)
                    .log("Error handling request: {}", exception.getMessage());
                break;
        }

        reply.publish(
            new FailedGetListingPublicDetailsByIdDTO(
                message.getParams(),
                new FailedGetListingPublicDetailsByIdErrorDTO(
                    code,
                    messageText
                )
            )
        );
    }
}
