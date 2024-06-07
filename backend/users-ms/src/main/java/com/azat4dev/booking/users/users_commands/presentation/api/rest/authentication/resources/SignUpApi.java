package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.application.commands.SignUp;
import com.azat4dev.booking.users.users_commands.application.handlers.SignUpHandler;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.utils.AuthenticateCurrentSession;
import com.azat4dev.booking.usersms.generated.server.api.CommandsSignUpApiDelegate;
import com.azat4dev.booking.usersms.generated.server.model.SignUpByEmailRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.SignUpByEmailResponseBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.TokensPairDTO;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public final class SignUpApi implements CommandsSignUpApiDelegate {

    private final Log logger = LogFactory.getLog(this.getClass());

    private final SignUpHandler signUpHandler;
    private final AuthenticateCurrentSession authenticateCurrentSession;

    private ResponseEntity<SignUpByEmailResponseBodyDTO> _signUpByEmail(SignUpByEmailRequestBodyDTO requestBody) throws SignUpHandler.Exception.UserWithSameEmailAlreadyExists {

        final var fullName = requestBody.getFullName();

        final UserId userId = signUpHandler.handle(
            new SignUp(
                new SignUp.FullName(
                    fullName.getFirstName(),
                    fullName.getLastName()
                ),
                requestBody.getEmail(),
                requestBody.getPassword()
            )
        );

        final var tokens = authenticateCurrentSession.execute(userId, new String[]{"ROLE_USER"});

        final var response = SignUpByEmailResponseBodyDTO.builder()
            .userId(userId.value())
            .tokens(
                TokensPairDTO.builder()
                    .access(tokens.accessToken())
                    .refresh(tokens.refreshToken())
                    .build()
            )
            .build();

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @Override
    public ResponseEntity<SignUpByEmailResponseBodyDTO> signUpByEmail(SignUpByEmailRequestBodyDTO requestBody) throws Exception {

        try {

            return _signUpByEmail(requestBody);

        } catch (SignUpHandler.Exception.UserWithSameEmailAlreadyExists e) {
            throw ControllerException.createError(HttpStatus.CONFLICT, e);
        }
    }
}
