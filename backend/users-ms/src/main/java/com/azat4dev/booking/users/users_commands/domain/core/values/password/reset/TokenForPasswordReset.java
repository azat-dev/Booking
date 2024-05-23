package com.azat4dev.booking.users.users_commands.domain.core.values.password.reset;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.utils.Assert;
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

    public static TokenForPasswordReset checkAndMakeFrom(String value) throws Exception {

        Assert.notNull(value, Exception::new);
        Assert.notBlank(value, Exception::new);

        return new TokenForPasswordReset(value);
    }

    @Override
    public String toString() {
        return value;
    }

    public static final class Exception extends DomainException {
        public Exception() {
            super("Invalid token");
        }

        @Override
        public String getCode() {
            return "InvalidToken";
        }
    }
}
