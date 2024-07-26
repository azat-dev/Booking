package com.azat4dev.booking.users.config.users_commands.infrastructure.bus;

import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers.*;
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

        final var mapFullName = new MapFullName();

        return List.of(
            new MapUserSignedUp(mapFullName, mapDateTime),
            new MapUserVerifiedEmail(),
            new MapVerificationEmailSent(),
            new MapFailedToSendVerificationEmail(),
            new MapSendVerificationEmail(mapFullName),
            new MapSentEmailForPasswordReset(),
            new MapUserDidResetPassword(),
            new MapGeneratedUserPhotoUploadUrl(),
            new MapUpdatedUserPhoto(),
            new MapFailedUpdateUserPhoto(),
            new MapFailedGenerateUserPhotoUploadUrl()
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
