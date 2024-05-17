package com.azat4dev.demobooking.users.users_commands.data;

import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializer;
import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializerImpl;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.*;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.*;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.verification.EmailVerificationToken;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.UploadFileFormData;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.UploadedFileData;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.PhotoFileExtension;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.BucketName;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectName;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.azat4dev.demobooking.users.users_commands.domain.EventHelpers.eventsFactory;
import static com.azat4dev.demobooking.users.users_commands.domain.UserHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;


public class DomainEventSerializerImplTests {

    DomainEventSerializer createSUT() {
        return new DomainEventSerializerImpl();
    }

    IdempotentOperationId anyIdempotentOperationId() throws IdempotentOperationId.Exception {
        return IdempotentOperationId.checkAndMakeFrom(UUID.randomUUID().toString());
    }

    @Test
    void test_serialize() throws MalformedURLException, PhotoFileExtension.InvalidPhotoFileExtensionException, IdempotentOperationId.Exception, BucketName.Exception {

        final var events = List.of(
            eventsFactory.issue(
                new UserCreated(
                    LocalDateTime.now(),
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
                new CompleteEmailVerification(
                    new EmailVerificationToken("token")
                )
            ),
            eventsFactory.issue(
                new ResetPasswordByEmail(
                    anyIdempotentOperationId(),
                    anyValidEmail()
                )
            ),
            eventsFactory.issue(
                new UserDidResetPassword(
                    anyValidUserId()
                )
            ),
            eventsFactory.issue(
                new CompletePasswordReset(
                    anyIdempotentOperationId(),
                    new EncodedPassword("password"),
                    TokenForPasswordReset.dangerouslyMakeFrom("passwordResetToken")
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
                    anyIdempotentOperationId(),
                    LocalDateTime.now()
                )
            ),
            eventsFactory.issue(
                new UpdateUserPhoto(
                    anyValidUserId(),
                    new UploadedFileData(
                        BucketName.checkAndMake("bucketName"),
                        MediaObjectName.checkAndMakeFrom("objectName")
                    )
                )
            ),

            eventsFactory.issue(
                new UpdatedUserPhoto(
                    anyIdempotentOperationId(),
                    anyValidUserId(),
                    new UserPhotoPath(
                        BucketName.checkAndMake("bucketName"),
                        MediaObjectName.checkAndMakeFrom("objectName")
                    ),
                    Optional.of(
                        new UserPhotoPath(
                            BucketName.checkAndMake("bucketName1"),
                            MediaObjectName.checkAndMakeFrom("objectName1")
                        )
                    )
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
