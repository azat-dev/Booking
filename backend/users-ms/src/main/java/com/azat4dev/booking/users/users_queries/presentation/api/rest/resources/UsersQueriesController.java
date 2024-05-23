package com.azat4dev.booking.users.users_queries.presentation.api.rest.resources;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_queries.domain.services.UsersQueryService;
import com.azat4dev.booking.users.users_queries.presentation.api.rest.dto.PersonalUserInfoDTO;
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

    @ExceptionHandler({UserId.WrongFormatException.class})
    public ResponseEntity<String> handleWrongUserId() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }

    @Override
    public ResponseEntity<PersonalUserInfoDTO> getCurrentUserInfo(JwtAuthenticationToken authentication) throws UserId.WrongFormatException {

        final var userId = UserId.checkAndMakeFrom(authentication.getName());
        final var userInfoResult = usersQueryService.getPersonalInfoById(userId);

        return userInfoResult.map(PersonalUserInfoDTO::from)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
