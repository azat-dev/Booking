package com.azat4dev.booking.shared.domain.values.files;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.utils.Assert;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class MediaObjectName {

    private static final String PATTERN = "[a-zA-Z0-9-_\\.]*";

    private final String value;

    private MediaObjectName(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public static MediaObjectName checkAndMakeFrom(String value) throws InvalidMediaObjectNameException {
        Assert.notNull(value, InvalidMediaObjectNameException::new);
        Assert.notBlank(value, InvalidMediaObjectNameException::new);
        Assert.hasPattern(value, PATTERN, InvalidMediaObjectNameException::new);

        return new MediaObjectName(value);
    }

    public static MediaObjectName dangerouslyMake(String value) {
        return new MediaObjectName(value);
    }

    // Exceptions
    public static final class InvalidMediaObjectNameException extends DomainException {
        public InvalidMediaObjectNameException() {
            super("Invalid media object name");
        }
    }
}
