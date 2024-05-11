package com.azat4dev.demobooking.users.users_queries.presentation.api.rest.resources;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_queries.domain.services.UsersQueryService;
import com.azat4dev.demobooking.users.users_queries.presentation.api.rest.dto.PersonalUserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Component
public class UsersQueriesController implements UsersQueiresResource {

    @Autowired
    UsersQueryService usersQueryService;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFound(ResourceNotFoundException ex) {
        return ex.getMessage();
    }

    @Override
    public ResponseEntity<PersonalUserInfoDTO> getCurrentUserInfo(JwtAuthenticationToken authentication) {

        final var userId = UserId.fromString(authentication.getName());
        final var userInfoResult = usersQueryService.getPersonalInfoById(userId);

        return userInfoResult.map(PersonalUserInfoDTO::from)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
