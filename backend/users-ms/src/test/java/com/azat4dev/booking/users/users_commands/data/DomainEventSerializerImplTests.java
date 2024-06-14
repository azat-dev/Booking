package com.azat4dev.booking.users.users_commands.data;

import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.*;
import com.azat4dev.booking.users.users_commands.data.repositories.DomainEventsSerializerImpl;
import com.azat4dev.booking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.users_commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.users_commands.domain.core.events.*;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.azat4dev.booking.users.users_commands.domain.EventHelpers.eventsFactory;
import static com.azat4dev.booking.users.users_commands.domain.UserHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;


public class DomainEventSerializerImplTests {

    DomainEventSerializer createSUT() {
        final var objectMapper = new ObjectMapper();
        // Register the Jdk8Module to handle Optional
        objectMapper.registerModule(new Jdk8Module());
        return new DomainEventsSerializerImpl(objectMapper);
    }

    IdempotentOperationId anyIdempotentOperationId() throws IdempotentOperationId.Exception {
        return IdempotentOperationId.checkAndMakeFrom(UUID.randomUUID().toString());
    }

    @Test
    void test_serialize() throws MalformedURLException, PhotoFileExtension.InvalidPhotoFileExtensionException, IdempotentOperationId.Exception, BucketName.Exception, MediaObjectName.InvalidMediaObjectNameException {

        final var now = LocalDateTime.now().withNano(0);

        final var events = List.of(
            eventsFactory.issue(
                new UserSignedUp(
                    now,
                    anyValidUserId(),
                    anyFullName(),
                    anyValidEmail(),
                    EmailVerificationStatus.NOT_VERIFIED
                )
            ),
            eventsFactory.issue(
                new SendVerificationEmail(
                    anyValidUserId(),
                    anyValidEmail(),
                    anyFullName(),
                    5
                )
            ),
            eventsFactory.issue(
                new UserVerifiedEmail(
                    anyValidUserId(),
                    anyValidEmail()
                )
            ),
            eventsFactory.issue(
                new VerificationEmailSent(
                    anyValidUserId(),
                    anyValidEmail()
                )
            ),
            eventsFactory.issue(
                new FailedToSendVerificationEmail(
                    anyValidUserId(),
                    anyValidEmail(),
                    5
                )
            ),
            eventsFactory.issue(
                new UserDidResetPassword(
                    anyValidUserId()
                )
            ),
            eventsFactory.issue(
                new GeneratedUserPhotoUploadUrl(
                    anyValidUserId(),
                    new UploadFileFormData(
                        URI.create("http://localhost").toURL(),
                        BucketName.makeWithoutChecks("bucketName"),
                        MediaObjectName.dangerouslyMake("objectName"),
                        Map.of("key", "value", "key2", "value2")
                    )
                )
            ),
            eventsFactory.issue(
                new FailedGenerateUserPhotoUploadUrl(
                    anyValidUserId(),
                    PhotoFileExtension.checkAndMakeFrom("png"),
                    100,
                    anyIdempotentOperationId()
                )
            ),

            eventsFactory.issue(
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
                )
            ),
            eventsFactory.issue(
                new FailedUpdateUserPhoto(
                    anyIdempotentOperationId(),
                    anyValidUserId(),
                    new UploadedFileData(
                        BucketName.checkAndMake("bucketName"),
                        MediaObjectName.checkAndMakeFrom("objectName")
                    )
                )
            ),

            eventsFactory.issue(
                new SentEmailForPasswordReset(
                    anyValidUserId(),
                    anyValidEmail()
                )
            )
        );

        for (final var event : events) {
            // Given
            final var sut = createSUT();

            // When
            final var serialized = sut.serialize(event);
            final var deserializedValue = sut.deserialize(serialized);

            // Then

            assertThat(serialized).isNotNull();
            assertThat(deserializedValue).isEqualTo(event);
        }
    }
}
