package com.azat4dev.booking.listingsms.config.common.infrastructure.bus;

import com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers.*;
import com.azat4dev.booking.shared.data.serializers.*;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Configuration
public class CommonBeansConfig {

    @Bean
    Mapper<LocalDateTime, String> mapLocalDateTime() {
        return new MapLocalDateTime();
    }

    @Bean
    MapPayload[] eventMappers(Mapper<LocalDateTime, String> mapDateTime) {

        final var mapGuestCapacity = new MapGuestsCapacity();
        final var mapAddress = new MapListingAddress();

        final var mapListingChange = new MapListingDetailsUpdatedChange(
            mapGuestCapacity,
            mapAddress
        );

        return new MapPayload[]{
            new MapNewListingAdded(),
            new MapFailedToAddNewListing(),
            new MapListingDetailsUpdated(mapListingChange, mapDateTime),
            new MapListingPublished(mapDateTime),
            new MapGeneratedUrlForUploadListingPhoto(),
            new MapFailedGenerateUrlForUploadListingPhoto(),
            new MapAddedNewPhotoToListing(),
        };
    }

    @Bean
    List<Class<DomainEventPayload>> domainEventPayload(
        MapPayload[] mappers
    ) {
        return Arrays.stream(mappers)
            .map(v -> (Class<DomainEventPayload>) v.getDomainClass())
            .toList();
    }

    @Bean
    DomainEventSerializer domainEventSerializer(
        ObjectMapper objectMapper,
        MapPayload[] mappers
    ) {
        return new DomainEventsSerializerJson(
            objectMapper,
            mappers
        );
    }
}
