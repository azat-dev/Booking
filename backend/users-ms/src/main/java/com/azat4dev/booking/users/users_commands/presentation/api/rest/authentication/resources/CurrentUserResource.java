package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.GenerateUserPhotoUploadUrlHandler;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.UpdateUserPhotoHandler;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.GenerateUploadUserPhotoUrlRequest;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.GenerateUploadUserPhotoUrlResponse;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.UpdateUserPhotoRequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/with-auth/users/current")
@Validated
public interface CurrentUserResource {

    @PostMapping("/get-upload-url-user-photo")
    ResponseEntity<GenerateUploadUserPhotoUrlResponse> generateUploadUserPhototUrl(
        @RequestBody GenerateUploadUserPhotoUrlRequest requestBody,
        JwtAuthenticationToken jwtAuthenticationToken
    ) throws UserId.WrongFormatException, GenerateUserPhotoUploadUrlHandler.Exception;

    @PostMapping("/update-photo")
    ResponseEntity<String> updateUserPhoto(
        JwtAuthenticationToken jwtAuthenticationToken,
        @RequestBody UpdateUserPhotoRequestBody requestBody
    ) throws UserId.WrongFormatException, UpdateUserPhotoHandler.Exception;
}
