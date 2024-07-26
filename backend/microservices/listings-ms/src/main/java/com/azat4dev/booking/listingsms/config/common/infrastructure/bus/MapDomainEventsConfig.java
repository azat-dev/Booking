package com.azat4dev.booking.listingsms.config.common.infrastructure.bus;

import com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers.*;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@AllArgsConstructor
public class MapDomainEventsConfig {

    private final Serializer<LocalDateTime, String> mapDateTime;

    @Bean
    List<MapDomainEvent> domainEventsMappers() {

        final var mapGuestCapacity = new MapGuestsCapacity();
        final var mapAddress = new MapListingAddress();

        final var mapListingChange = new MapListingDetailsUpdatedChange(
            mapGuestCapacity,
            mapAddress
        );

        return List.of(
            new MapNewListingAdded(),
            new MapFailedToAddNewListing(),
            new MapListingDetailsUpdated(mapListingChange, mapDateTime),
            new MapListingPublished(mapDateTime),
            new MapGeneratedUrlForUploadListingPhoto(),
            new MapFailedGenerateUrlForUploadListingPhoto(),
            new MapAddedNewPhotoToListing()
        );
    }

    @Bean
    Set<Class<? extends DomainEventPayload>> mappedDomainEventsClasses(List<MapDomainEvent> mappers) {
        Set<Class<? extends DomainEventPayload>> result = new HashSet<>();
        for (MapDomainEvent<?, ?> mapper : mappers) {
            result.add(mapper.getOriginalClass());
        }

        return result;
    }
}
