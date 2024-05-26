package com.azat4dev.booking.users.users_commands.domain.handlers.user;

import com.azat4dev.booking.shared.domain.event.DomainEventsBus;
import com.azat4dev.booking.users.users_commands.domain.UserHelpers;
import com.azat4dev.booking.users.users_commands.domain.core.commands.GenerateUserPhotoUploadUrl;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.files.UploadFileFormData;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.PhotoFileExtension;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.GenerateUserPhotoObjectName;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.photo.GenerateUrlForUploadUserPhoto;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.photo.GenerateUrlForUploadUserPhotoImpl;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.BucketName;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectName;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectsBucket;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

public class GenerateUrlForUploadUserPhotoTests {

    SUT createSUT() {

        final var generateUserPhotoObjectName = mock(GenerateUserPhotoObjectName.class);
        final var usersPhotoBucket = mock(MediaObjectsBucket.class);
        final var bus = mock(DomainEventsBus.class);

        final var expiresIn = 5;

        return new SUT(
            new GenerateUrlForUploadUserPhotoImpl(
                expiresIn,
                generateUserPhotoObjectName,
                usersPhotoBucket,
                bus
            ),
            generateUserPhotoObjectName,
            usersPhotoBucket,
            bus,
            expiresIn
        );
    }

    IdempotentOperationId anyOperationId() {
        return new IdempotentOperationId(UUID.randomUUID().toString());
    }

    @Test
    public void test_handle_givenFileWithinLimit_thenGenerateUploadUrlData() throws Exception {

        // Given
        final var sut = createSUT();
        final var operationId = anyOperationId();
        final var fileExtension = PhotoFileExtension.checkAndMakeFrom("jpg");
        final var fileSize = GenerateUserPhotoUploadUrl.MAX_FILE_SIZE;
        final var userId = UserHelpers.anyValidUserId();

        final var objectName = MediaObjectName.dangerouslyMake("testObjectName");

        final var formData = new UploadFileFormData(
            URI.create("http://test.com").toURL(),
            BucketName.makeWithoutChecks("testBucket"),
            objectName,
            Map.of("key", "value", "key2", "value2")
        );

        given(sut.generateUserPhotoObjectName.execute(any(), any()))
            .willReturn(objectName);

        given(sut.usersPhotoBucket.generateUploadFormData(eq(objectName), eq(sut.expiresIn), any()))
            .willReturn(formData);


        // When
        final var result = sut.generateUrl.execute(
            operationId,
            userId,
            fileExtension,
            fileSize
        );

        // Then
        then(sut.generateUserPhotoObjectName())
            .should(times(1))
            .execute(userId, fileExtension);

        then(sut.usersPhotoBucket)
            .should(times(1))
            .generateUploadFormData(
                eq(objectName),
                eq(sut.expiresIn),
                any()
            );

        then(sut.bus).should(times(1))
            .publish(result);

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.formData()).isEqualTo(formData);
    }

    record SUT(
        GenerateUrlForUploadUserPhoto generateUrl,
        GenerateUserPhotoObjectName generateUserPhotoObjectName,
        MediaObjectsBucket usersPhotoBucket,
        DomainEventsBus bus,
        int expiresIn
    ) {
    }
}