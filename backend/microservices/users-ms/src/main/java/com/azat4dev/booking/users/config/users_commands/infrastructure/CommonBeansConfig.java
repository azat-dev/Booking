package com.azat4dev.booking.users.config.users_commands.infrastructure;

import com.azat4dev.booking.shared.data.serializers.*;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.utils.SystemTimeProvider;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers.*;
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
    TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }

    @Bean
    Mapper<LocalDateTime, String> mapLocalDateTime() {
        return new MapLocalDateTime();
    }

    @Bean
    MapPayload[] eventMappers(Mapper<LocalDateTime, String> mapDateTime) {

        final var mapFullName = new MapFullName();

        return new MapPayload[]{
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
