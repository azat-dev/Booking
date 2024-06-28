package com.azat4dev.booking.shared.domain.values.files;

import com.azat4dev.booking.shared.domain.DomainException;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(of = "value")
public final class PhotoFileExtension {

    public static final List<FileExtension> ALLOWED_EXTENSIONS = List.of(
        FileExtension.JPG,
        FileExtension.JPEG,
        FileExtension.PNG
    );

    private final FileExtension value;

    private PhotoFileExtension(FileExtension value) {
        this.value = value;
    }

    public static PhotoFileExtension dangerouslyMakeFrom(String value) {
        return new PhotoFileExtension(FileExtension.dangerouslyMakeFrom(value));
    }

    public static PhotoFileExtension checkAndMakeFrom(String value) throws InvalidPhotoFileExtensionException {

        try {
            final var extension = FileExtension.checkAndMakeFrom(value);

            ALLOWED_EXTENSIONS.stream()
                .filter(allowed -> allowed.equals(extension))
                .findAny()
                .orElseThrow(InvalidPhotoFileExtensionException::new);

            return new PhotoFileExtension(extension);

        } catch (FileExtension.Exception e) {
            throw new InvalidPhotoFileExtensionException();
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    // Exceptions

    public static final class InvalidPhotoFileExtensionException extends DomainException {
        public InvalidPhotoFileExtensionException() {
            super("Invalid photo file extension. Allowed extensions: " + ALLOWED_EXTENSIONS.toString());
        }

        @Override
        public String getCode() {
            return "InvalidPhotoFileExtension";
        }
    }
}
