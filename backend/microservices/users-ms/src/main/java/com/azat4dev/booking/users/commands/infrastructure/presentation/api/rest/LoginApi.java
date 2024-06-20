package com.azat4dev.booking.users.commands.infrastructure.presentation.api.rest;

import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.users.commands.application.commands.LoginByEmail;
import com.azat4dev.booking.users.commands.application.handlers.LoginByEmailHandler;
import com.azat4dev.booking.users.commands.infrastructure.presentation.api.rest.utils.AuthenticateCurrentSession;
import com.azat4dev.booking.usersms.generated.server.api.CommandsLoginApiDelegate;
import com.azat4dev.booking.usersms.generated.server.model.LoginByEmailRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.LoginByEmailResponseBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.TokensPairDTO;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public final class LoginApi implements CommandsLoginApiDelegate {

    private final Log logger = LogFactory.getLog(this.getClass());

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
            if (logger.isInfoEnabled()) {
                logger.info("User with email " + loginByEmailRequestBody.getEmail() + " logged in");
            }

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
            if (logger.isInfoEnabled()) {
                logger.info("User with email " + loginByEmailRequestBody.getEmail() + " failed to log in");
            }
            throw new ControllerException(HttpStatus.FORBIDDEN, e);
        }
    }
}
