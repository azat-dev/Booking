package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.resources;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/public")
@Validated
public interface EmailVerificationResource {

    @GetMapping("/verify-email")
    ResponseEntity<String> verifyEmail(
        @RequestParam("token") String token,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception;
}
