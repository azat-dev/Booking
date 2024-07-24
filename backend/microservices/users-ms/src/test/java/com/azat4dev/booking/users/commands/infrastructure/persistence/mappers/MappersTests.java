package com.azat4dev.booking.users.commands.infrastructure.persistence.mappers;

import com.azat4dev.booking.shared.data.serializers.MapLocalDateTime;
import com.azat4dev.booking.shared.data.serializers.MapDomainEvent;
import com.azat4dev.booking.shared.data.serializers.Serializer;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.*;
import com.azat4dev.booking.users.commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.commands.domain.core.events.*;
import com.azat4dev.booking.users.commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers.*;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.azat4dev.booking.users.commands.domain.UserHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;

class MappersTests {

    IdempotentOperationId anyIdempotentOperationId() throws IdempotentOperationId.Exception {
        return IdempotentOperationId.checkAndMakeFrom(UUID.randomUUID().toString());
    }

    @Test
    void test_mappers() throws Exception {

        // Given
        final var mapFullName = new MapFullName();
        final var mapDateTime = new MapLocalDateTime();

        final var mappers = new MapDomainEvent[]{
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

        final var now = LocalDateTime.now();

        final var events = new DomainEventPayload[]{

            new UserSignedUp(
                now,
                anyValidUserId(),
                anyFullName(),
                anyValidEmail(),
                EmailVerificationStatus.NOT_VERIFIED
            ),
            new SendVerificationEmail(
                anyValidUserId(),
                anyValidEmail(),
                anyFullName(),
                5
            ),
            new UserVerifiedEmail(
                anyValidUserId(),
                anyValidEmail()
            ),
            new VerificationEmailSent(
                anyValidUserId(),
                anyValidEmail()
            ),

            new FailedToSendVerificationEmail(
                anyValidUserId(),
                anyValidEmail(),
                5
            ),

            new UserDidResetPassword(
                anyValidUserId()
            ),

            new GeneratedUserPhotoUploadUrl(
                anyValidUserId(),
                new UploadFileFormData(
                    URI.create("http://localhost").toURL(),
                    BucketName.makeWithoutChecks("bucketName"),
                    MediaObjectName.dangerouslyMake("objectName"),
                    Map.of("key", "value", "key2", "value2")
                )
            ),

            new FailedGenerateUserPhotoUploadUrl(
                anyValidUserId(),
                PhotoFileExtension.checkAndMakeFrom("png"),
                100,
                anyIdempotentOperationId()
            ),


            new UpdatedUserPhoto(
                anyValidUserId(),
                new UserPhotoPath(
                    BucketName.checkAndMake("bucketname"),
                    MediaObjectName.checkAndMakeFrom("objectName")
                ),
                Optional.of(
                    new UserPhotoPath(
                        BucketName.checkAndMake("bucketname1"),
                        MediaObjectName.checkAndMakeFrom("objectName1")
                    )
                )
            ),

            new FailedUpdateUserPhoto(
                anyIdempotentOperationId(),
                anyValidUserId(),
                new UploadedFileData(
                    BucketName.checkAndMake("bucketName"),
                    MediaObjectName.checkAndMakeFrom("objectName")
                )
            ),

            new SentEmailForPasswordReset(
                anyValidUserId(),
                anyValidEmail()
            )
        };

        for (final var mapper : mappers) {
            final var event = Arrays.stream(events).filter(e -> e.getClass()
                    .equals(mapper.getOriginalClass()))
                .findFirst().get();

            // When
            test_mapping(mapper, event);
        }
    }

    <E extends DomainEventPayload, D> void test_mapping(Serializer<E, D> serializer, E event) {

        // Given

        // When
        final var dto = serializer.serialize(event);
        final var deserializedEvent = serializer.deserialize(dto);

        // Then
        assertThat(deserializedEvent).isEqualTo(event);
    }
}
