package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.common.domain.event.EventIdGenerator;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.UpdateUserPhoto;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.InitialUserPhotoFileName;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.users.UpdateUserPhotoHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/with-auth/users/current")
@Validated
public class CurrentUserResource {

    @Autowired
    UpdateUserPhotoHandler updateUserPhotoHandler;

    @Autowired
    private TimeProvider timeProvider;

    @Autowired
    private EventIdGenerator eventIdGenerator;

    @PostMapping("/get-upload-url-user-photo")
    ResponseEntity<String> updatePhoto(
        @RequestParam("photo") MultipartFile photo,
        JwtAuthenticationToken jwtAuthenticationToken
    ) throws IOException {

        final var userId = UserId.fromString(jwtAuthenticationToken.getName());
        final var fileName = InitialUserPhotoFileName.checkAndMakeFrom(photo.getOriginalFilename());

        final var command = UpdateUserPhoto.checkAndMakeFrom(
            userId,
            photo.getSize(),
            fileName,
            () -> {
                try {
                    return photo.getInputStream().readAllBytes();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        );

        updateUserPhotoHandler.handle(
            command,
            eventIdGenerator.generate(),
            timeProvider.currentTime()
        );
        return ResponseEntity.ok("Photo updated");
    }
}
