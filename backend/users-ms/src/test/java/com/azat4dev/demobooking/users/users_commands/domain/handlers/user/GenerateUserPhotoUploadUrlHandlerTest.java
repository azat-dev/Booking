package com.azat4dev.demobooking.users.users_commands.domain.handlers.user;

import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.GenerateUserPhotoUploadUrl;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.UploadFileFormData;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.PhotoFileExtension;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.users.GenerateUserPhotoObjectName;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.users.GenerateUserPhotoUploadUrlHandler;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectName;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectsBucket;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

public class GenerateUserPhotoUploadUrlHandlerTest {

    SUT createSUT() {

        final var generateUserPhotoObjectName = mock(GenerateUserPhotoObjectName.class);
        final var usersPhotoBucket = mock(MediaObjectsBucket.class);
        final var bus = mock(DomainEventsBus.class);

        final var expiresIn = 5;

        return new SUT(
            new GenerateUserPhotoUploadUrlHandler(
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
        return new IdempotentOperationId(UUID.randomUUID());
    }

    @Test
    public void test_handle_givenFileWithinLimit_thenGenerateUploadUrlData() throws Exception {

        // Given
        final var sut = createSUT();
        final var command = new GenerateUserPhotoUploadUrl(
            UserHelpers.anyValidUserId(),
            PhotoFileExtension.checkAndMakeFrom("jpg"),
            GenerateUserPhotoUploadUrl.MAX_FILE_SIZE,
            anyOperationId(),
            LocalDateTime.now()
        );

        final var objectName = MediaObjectName.dangerouslyMake("testObjectName");

        final var formData = new UploadFileFormData(
            URI.create("http://test.com").toURL(),
            objectName,
            Map.of("key", "value", "key2", "value2")
        );

        given(sut.generateUserPhotoObjectName.execute(any(), any()))
            .willReturn(objectName);

        given(sut.usersPhotoBucket.generateUploadFormData(eq(objectName), eq(sut.expiresIn), any()))
            .willReturn(formData);


        // When
        final var result = sut.handler.handle(command);

        // Then
        then(sut.generateUserPhotoObjectName())
            .should(times(1))
            .execute(command.getUserId(), command.getFileExtension());

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
        assertThat(result.userId()).isEqualTo(command.getUserId());
        assertThat(result.formData()).isEqualTo(formData);
    }

    record SUT(
        GenerateUserPhotoUploadUrlHandler handler,
        GenerateUserPhotoObjectName generateUserPhotoObjectName,
        MediaObjectsBucket usersPhotoBucket,
        DomainEventsBus bus,
        int expiresIn
    ) {
    }
}