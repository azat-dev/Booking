package com.azat4dev.booking.users.commands.infrastructure.presentation.api.rest;

import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.users.commands.application.commands.LoginByEmail;
import com.azat4dev.booking.users.commands.application.handlers.LoginByEmailHandler;
import com.azat4dev.booking.users.commands.infrastructure.presentation.api.rest.utils.AuthenticateCurrentSession;
import com.azat4dev.booking.usersms.generated.server.api.CommandsLoginApiDelegate;
import com.azat4dev.booking.usersms.generated.server.model.LoginByEmailRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.LoginByEmailResponseBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.TokensPairDTO;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Observed
@Component
@AllArgsConstructor
public class LoginApi implements CommandsLoginApiDelegate {

    private final LoginByEmailHandler loginByEmailHandler;
    private final AuthenticateCurrentSession authenticateCurrentSession;


    @Override
    public ResponseEntity<LoginByEmailResponseBodyDTO> loginByEmail(LoginByEmailRequestBodyDTO loginByEmailRequestBody) throws Exception {

        try {
            final var user = loginByEmailHandler.handle(
                new LoginByEmail(
                    loginByEmailRequestBody.getEmail(),
                    loginByEmailRequestBody.getPassword()
                )
            );

            final var tokens = authenticateCurrentSession.execute(user.getId(), new String[]{"ROLE_USER"});
            log.atInfo().addKeyValue("userId", user::getId).log("User logged in");

            return ResponseEntity.ok(
                LoginByEmailResponseBodyDTO.builder()
                    .userId(user.getId().value())
                    .tokens(
                        TokensPairDTO.builder()
                            .access(tokens.accessToken())
                            .refresh(tokens.refreshToken())
                            .build()
                    )
                    .build()
            );

        } catch (LoginByEmailHandler.Exception.WrongCredentials e) {
            log.atInfo().log("User failed to log in");
            throw new ControllerException(HttpStatus.FORBIDDEN, e);
        }
    }
}
