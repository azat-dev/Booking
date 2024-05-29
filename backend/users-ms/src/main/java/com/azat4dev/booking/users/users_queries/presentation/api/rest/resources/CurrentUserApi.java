package com.azat4dev.booking.users.users_queries.presentation.api.rest.resources;

import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.users.users_queries.domain.services.UsersQueryService;
import com.azat4dev.booking.users.users_queries.presentation.api.utils.CurrentAuthenticatedUserIdProvider;
import com.azat4dev.booking.usersms.generated.server.api.QueriesCurrentUserApiDelegate;
import com.azat4dev.booking.usersms.generated.server.model.EmailVerificationStatusDTO;
import com.azat4dev.booking.usersms.generated.server.model.FullNameDTO;
import com.azat4dev.booking.usersms.generated.server.model.PersonalUserInfoDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@AllArgsConstructor
public class CurrentUserApi implements QueriesCurrentUserApiDelegate {

    private final CurrentAuthenticatedUserIdProvider currentUserId;
    private final UsersQueryService usersQueryService;

    @Override
    public ResponseEntity<PersonalUserInfoDTO> getCurrentUser() {

        final var userId = currentUserId.get()
            .orElseThrow(() -> new ControllerException(HttpStatus.UNAUTHORIZED, "User not authenticated"));

        final var userInfo = usersQueryService.getPersonalInfoById(userId)
            .orElseThrow(() -> new ControllerException(HttpStatus.NOT_FOUND, "User not found"));

        final var fullName = userInfo.fullName();

        final var dto = PersonalUserInfoDTO.builder()
            .id(userInfo.id().value())
            .email(userInfo.email())
            .emailVerificationStatus(
                EmailVerificationStatusDTO.valueOf(userInfo.emailVerificationStatus().name())
            ).fullName(
                FullNameDTO.builder()
                    .firstName(fullName.getFirstName().getValue())
                    .lastName(fullName.getLastName().getValue())
                    .build()
            ).build();

        return ResponseEntity.ok(dto);
    }
}
