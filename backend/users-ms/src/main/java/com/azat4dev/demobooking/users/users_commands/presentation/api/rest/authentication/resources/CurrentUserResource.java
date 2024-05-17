package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.GenerateUploadUserPhotoUrlRequest;
import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.GenerateUploadUserPhotoUrlResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/with-auth/users/current")
@Validated
public interface CurrentUserResource {

    @PostMapping("/get-upload-url-user-photo")
    ResponseEntity<GenerateUploadUserPhotoUrlResponse> generateUploadUserPhototUrl(
        @RequestBody GenerateUploadUserPhotoUrlRequest requestBody,
        JwtAuthenticationToken jwtAuthenticationToken
    );

    @PostMapping("/update-photo")
    ResponseEntity<String> uploadNewUserPhoto(
        @RequestParam("photo") MultipartFile photo,
        JwtAuthenticationToken jwtAuthenticationToken
    );
}
