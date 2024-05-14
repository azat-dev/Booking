package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventsFactory;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.UpdateUserPhoto;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.InitialUserPhotoFileName;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.UpdateUserPhotoHandler;
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
@RequestMapping("/api/with-auth/user")
@Validated
public class UserResource {

    @Autowired
    DomainEventsFactory eventsFactory;

    @Autowired
    UpdateUserPhotoHandler updateUserPhotoHandler;

    @PostMapping("/update-photo")
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

        final var event = (DomainEventNew<UpdateUserPhoto>) eventsFactory.issue(command);

        updateUserPhotoHandler.handle(event);
        return ResponseEntity.ok("Photo updated");
    }
}
