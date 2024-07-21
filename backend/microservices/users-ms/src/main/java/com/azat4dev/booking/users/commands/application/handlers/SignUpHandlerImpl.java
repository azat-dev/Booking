package com.azat4dev.booking.users.commands.application.handlers;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.shared.domain.values.user.UserIdFactory;
import com.azat4dev.booking.users.commands.application.commands.SignUp;
import com.azat4dev.booking.users.commands.domain.core.commands.NewUserData;
import com.azat4dev.booking.users.commands.domain.core.entities.User;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.password.Password;
import com.azat4dev.booking.users.commands.domain.core.values.user.FirstName;
import com.azat4dev.booking.users.commands.domain.core.values.user.FullName;
import com.azat4dev.booking.users.commands.domain.core.values.user.LastName;
import com.azat4dev.booking.users.commands.domain.handlers.users.Users;
import com.azat4dev.booking.users.commands.domain.interfaces.services.PasswordService;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Observed
@Slf4j
@AllArgsConstructor
public class SignUpHandlerImpl implements SignUpHandler {

    private final UserIdFactory userIdFactory;
    private final PasswordService passwordService;
    private final Users users;

    public UserId handle(SignUp command) throws ValidationException, SignUpHandler.Exception.UserWithSameEmailAlreadyExists {

        try {
            final var firstName = FirstName.checkAndMakeFromString(command.fullName().firstName());
            final var lastName = LastName.checkAndMakeFromString(command.fullName().lastName());
            final var fullName = FullName.makeWithChecks(firstName, lastName);
            final var email = EmailAddress.checkAndMakeFromString(command.email());

            final var password = Password.checkAndMakeFromString(command.password());
            final var encodedPassword = passwordService.encodePassword(password);

            final var userId = userIdFactory.generateNewUserId();

            final var newUserData = new NewUserData(
                userId,
                fullName,
                email,
                encodedPassword
            );

            users.createNew(newUserData);
            log.atInfo().addArgument(userId).log("User signed up: {}");
            return userId;

        } catch (FirstName.ValidationException e) {
            log.atWarn().addKeyValue("code", e.getCode()).log("Invalid first name");
            throw ValidationException.withPath("firstName", e);
        } catch (LastName.ValidationException e) {
            log.atWarn().addKeyValue("code", e.getCode()).log("Invalid last name");
            throw ValidationException.withPath("lastName", e);
        } catch (EmailAddress.ValidationException e) {
            log.atWarn().addKeyValue("code", e.getCode()).log("Invalid email");
            throw ValidationException.withPath("email", e);
        } catch (Password.Exception e) {
            log.atWarn().addKeyValue("code", e.getCode()).log("Invalid password");
            throw ValidationException.withPath("password", e);
        } catch (Users.Exception.UserWithSameEmailAlreadyExists e) {
            log.atWarn().addKeyValue("code", e.getCode()).log("User with same email already exists");
            throw new SignUpHandler.Exception.UserWithSameEmailAlreadyExists();
        } catch (FullName.Exception e) {
            log.atWarn().addKeyValue("code", e.getCode()).log("Invalid full name");
            throw ValidationException.withPath("fullName", e);
        } catch (User.Exception e) {
            log.atError().setCause(e).log("Failed to create new user");
            throw new RuntimeException(e);
        }
    }
}
