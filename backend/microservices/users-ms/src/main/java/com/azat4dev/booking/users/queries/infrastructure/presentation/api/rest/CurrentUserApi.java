package com.azat4dev.booking.users.queries.infrastructure.presentation.api.rest;

import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.presentation.CurrentAuthenticatedUserIdProvider;
import com.azat4dev.booking.users.queries.domain.services.UsersQueryService;
import com.azat4dev.booking.users.queries.infrastructure.presentation.api.rest.mappers.MapPersonalUserInfoToDTO;
import com.azat4dev.booking.usersms.generated.server.api.QueriesCurrentUserApiDelegate;
import com.azat4dev.booking.usersms.generated.server.model.PersonalUserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CurrentUserApi implements QueriesCurrentUserApiDelegate {

    private final CurrentAuthenticatedUserIdProvider currentUserId;
    private final UsersQueryService usersQueryService;
    private final MapPersonalUserInfoToDTO mapToDTO;

    @Override
    public ResponseEntity<PersonalUserInfoDTO> getCurrentUser() throws Exception {

        final var userId = currentUserId.get()
            .orElseThrow(() -> {
                log.atInfo().log("User not authenticated");
                return new ControllerException(HttpStatus.UNAUTHORIZED, "User not authenticated");

            });

        final var userInfo = usersQueryService.getPersonalInfoById(userId)
            .orElseThrow(() -> {
                log.atInfo().addKeyValue("userId", userId).log("User not found");
                return new ControllerException(HttpStatus.NOT_FOUND, "User not found");
            });

        final var dto = mapToDTO.map(userInfo);
        log.atDebug().addKeyValue("dto", dto).log("User info");
        return ResponseEntity.ok(dto);
    }
}
