package com.azat4dev.demobooking.users.users_queries.presentation.api.rest.resources;

import com.azat4dev.demobooking.users.users_queries.presentation.api.rest.dto.CurrentUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/with-auth/users")
@Validated
public interface UsersQueiresResource {

    @GetMapping("current")
    ResponseEntity<CurrentUserDTO> getUser(JwtAuthenticationToken authentication);
}
