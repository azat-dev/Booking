package com.azat4dev.demobooking.users.users_commands.domain.core.values.user;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.FileExtension;
import lombok.EqualsAndHashCode;

import java.util.Arrays;

@EqualsAndHashCode(callSuper = true)
public class PhotoFileExtension extends FileExtension {

    public static final FileExtension[] ALLOWED_EXTENSIONS = new FileExtension[]{
        FileExtension.JPG,
        FileExtension.JPEG,
        FileExtension.PNG
    };

    private PhotoFileExtension(String value) {
        super(value);
    }

    public static PhotoFileExtension dangerouslyMakeFrom(String value) {
        return new PhotoFileExtension(value);
    }

    public static PhotoFileExtension checkAndMakeFrom(String value) {

        try {
            final var extension = FileExtension.checkAndMakeFrom(value);

            Arrays.stream(ALLOWED_EXTENSIONS)
                .filter(allowed -> allowed.equals(extension))
                .findAny()
                .orElseThrow(InvalidPhotoFileExtensionException::new);
            return new PhotoFileExtension(value);
        } catch (FileExtension.EmptyFileExtensionException e) {
            throw new InvalidPhotoFileExtensionException();
        }
    }

    // Exceptions

    public static final class InvalidPhotoFileExtensionException extends RuntimeException {
        public InvalidPhotoFileExtensionException() {
            super("Invalid photo file extension. Allowed extensions: " + Arrays.toString(ALLOWED_EXTENSIONS));
        }
    }
}
