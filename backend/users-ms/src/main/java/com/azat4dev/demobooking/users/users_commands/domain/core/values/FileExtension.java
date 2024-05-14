package com.azat4dev.demobooking.users.users_commands.domain.core.values;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString(of="value")
@Getter
@EqualsAndHashCode(of="value")
public final class FileExtension {

    private final String value;

    private FileExtension(String value) {
        this.value = value;
    }

    public static FileExtension dangerouslyMakeFrom(String value) {
        return new FileExtension(value);
    }

    public static FileExtension checkAndMakeFrom(String value) {
        final var cleanedValue = value.trim().toLowerCase();

        Assert.notNull(cleanedValue, EmptyFileExtensionException::new);
        Assert.notBlank(cleanedValue, EmptyFileExtensionException::new);

        return new FileExtension(cleanedValue);
    }

    // Exceptions

    public static final class EmptyFileExtensionException extends DomainException {
        public EmptyFileExtensionException() {
            super("File extension is required");
        }

        @Override
        public String getCode() {
            return "EmptyFileExtension";
        }
    }
}
