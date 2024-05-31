package com.azat4dev.booking.users.users_commands.domain.core.values.user;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.values.files.FileExtension;
import lombok.EqualsAndHashCode;

import java.util.Arrays;

@EqualsAndHashCode(of = "value")
public final class PhotoFileExtension {

    public static final FileExtension[] ALLOWED_EXTENSIONS = new FileExtension[]{
        FileExtension.JPG,
        FileExtension.JPEG,
        FileExtension.PNG
    };
    private final FileExtension value;

    private PhotoFileExtension(FileExtension value) {
        this.value = value;
    }

    public static PhotoFileExtension dangerouslyMakeFrom(String value) {
        return new PhotoFileExtension(FileExtension.dangerouslyMakeFrom(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public static PhotoFileExtension checkAndMakeFrom(String value) throws InvalidPhotoFileExtensionException {

        try {
            final var extension = FileExtension.checkAndMakeFrom(value);

            Arrays.stream(ALLOWED_EXTENSIONS)
                .filter(allowed -> allowed.equals(extension))
                .findAny()
                .orElseThrow(InvalidPhotoFileExtensionException::new);

            return new PhotoFileExtension(extension);

        } catch (FileExtension.Exception e) {
            throw new InvalidPhotoFileExtensionException();
        }
    }

    // Exceptions

    public static final class InvalidPhotoFileExtensionException extends DomainException {
        public InvalidPhotoFileExtensionException() {
            super("Invalid photo file extension. Allowed extensions: " + Arrays.toString(ALLOWED_EXTENSIONS));
        }

        @Override
        public String getCode() {
            return "InvalidPhotoFileExtension";
        }
    }
}
