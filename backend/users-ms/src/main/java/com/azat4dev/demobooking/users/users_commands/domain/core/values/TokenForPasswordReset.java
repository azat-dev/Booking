package com.azat4dev.demobooking.users.users_commands.domain.core.values;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TokenForPasswordReset {
    private final String value;

    private TokenForPasswordReset(String value) {
        this.value = value;
    }

    public static TokenForPasswordReset dangerouslyMakeFrom(String value) {
        return new TokenForPasswordReset(value);
    }

    public static TokenForPasswordReset checkAndMakeFrom(String value) {

        Assert.notNull(value, InvalidTokenException::new);
        Assert.notBlank(value, InvalidTokenException::new);

        return new TokenForPasswordReset(value);
    }

    @Override
    public String toString() {
        return value;
    }

    static final class InvalidTokenException extends DomainException {
        public InvalidTokenException() {
            super("Invalid token");
        }

        @Override
        public String getCode() {
            return "InvalidToken";
        }
    }
}
