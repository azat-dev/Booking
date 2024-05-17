package com.azat4dev.demobooking.users.users_commands.domain.core.values.files;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;
import lombok.Getter;

import java.util.List;

@Getter
public final class InitialUserPhotoFileName {

    public static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "bmp", "webp");

    private final String name;
    private final FileExtension extension;

    private InitialUserPhotoFileName(String name, FileExtension extension) {
        this.name = name;
        this.extension = extension;
    }

    // Exceptions

    public static InitialUserPhotoFileName checkAndMakeFrom(String value) throws Exception {

        Assert.notNull(value, Exception.NullValue::new);

        final var extension = parseExtension(value);
        Assert.notNull(extension, Exception.NullValue::new);

        final var name = parseName(value);
        Assert.notBlank(name, Exception.InvalidName::new);

        if (ALLOWED_EXTENSIONS.contains(extension.getValue())) {
            throw new Exception.InvalidExtension(extension.getValue());
        }

        return new InitialUserPhotoFileName(name, extension);
    }

    private static FileExtension parseExtension(String fileName) throws Exception {

        final var value = fileName.substring(fileName.lastIndexOf('.') + 1);

        try {
            return FileExtension.checkAndMakeFrom(value);
        } catch (FileExtension.Exception e) {
            throw new Exception.InvalidExtension(value);
        }
    }

    private static String parseName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    // Exceptions

    public static abstract sealed class Exception extends DomainException
        permits Exception.InvalidExtension, Exception.NullValue, Exception.InvalidName {

        Exception(String message) {
            super(message);
        }

        public static final class InvalidExtension extends Exception {
            public InvalidExtension(String value) {
                super(String.format("Invalid initial user photo file name: %s. Allowed extensions: ", value, ALLOWED_EXTENSIONS));
            }
        }

        public static final class NullValue extends Exception {
            public NullValue() {
                super("Initial user photo file name cannot be null");
            }
        }

        public static final class InvalidName extends Exception {
            public InvalidName() {
                super("Initial user photo file name cannot be empty");
            }
        }
    }
}
