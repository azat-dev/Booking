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

    public static InitialUserPhotoFileName checkAndMakeFrom(String value) {

        Assert.notNull(value, NullValueException::new);

        final var extension = parseExtension(value);
        Assert.notNull(extension, () -> new InvalidExtensionException(extension.getValue()));

        final var name = parseName(value);
        Assert.notBlank(name, InvalidNameExtension::new);

        ALLOWED_EXTENSIONS.forEach(allowedExtension -> {
            if (!extension.getValue().equals(allowedExtension)) {
                throw new InvalidExtensionException(extension.getValue());
            }
        });

        return new InitialUserPhotoFileName(name, extension);
    }

    private static FileExtension parseExtension(String fileName) {

        final var value = fileName.substring(fileName.lastIndexOf('.') + 1);

        try {
            return FileExtension.checkAndMakeFrom(value);
        } catch (FileExtension.EmptyFileExtensionException e) {
            throw new InvalidExtensionException(value);
        }
    }

    private static String parseName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    // Exceptions

    public static abstract class ValidationException extends DomainException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static final class InvalidExtensionException extends ValidationException {
        public InvalidExtensionException(String value) {
            super(String.format("Invalid initial user photo file name: %s. Allowed extensions: ", value, ALLOWED_EXTENSIONS));
        }

        @Override
        public String getCode() {
            return "InvalidExtension";
        }
    }

    public static final class NullValueException extends ValidationException {
        public NullValueException() {
            super("Initial user photo file name cannot be null");
        }

        @Override
        public String getCode() {
            return "NullValue";
        }
    }

    public static final class InvalidNameExtension extends ValidationException {
        public InvalidNameExtension() {
            super("Initial user photo file name cannot be empty");
        }

        @Override
        public String getCode() {
            return "InvalidFileName";
        }
    }
}
