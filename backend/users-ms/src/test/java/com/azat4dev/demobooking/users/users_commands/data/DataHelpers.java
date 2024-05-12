package com.azat4dev.demobooking.users.users_commands.data;

import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.NewUserData;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataHelpers {

    public static NewUserData anyNewUserData() {
        return new NewUserData(
            UserHelpers.anyValidUserId(),
            LocalDateTime.now(),
            UserHelpers.anyValidEmail(),
            UserHelpers.anyFullName(),
            UserHelpers.anyEncodedPassword(),
            EmailVerificationStatus.NOT_VERIFIED
        );
    }
}
