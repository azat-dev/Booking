package com.azat4dev.booking.shared.domain.values.files;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.utils.Assert;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "value")
public class FileExtension {

    // Media extensions
    public static final FileExtension JPG = FileExtension.dangerouslyMakeFrom("jpg");
    public static final FileExtension JPEG = FileExtension.dangerouslyMakeFrom("jpeg");
    public static final FileExtension PNG = FileExtension.dangerouslyMakeFrom("png");
    public static final FileExtension WEBP = FileExtension.dangerouslyMakeFrom("webp");
    public static final FileExtension GIF = FileExtension.dangerouslyMakeFrom("gif");

    // Exceptions
    private final String value;

    protected FileExtension(String value) {
        this.value = value;
    }

    public static FileExtension dangerouslyMakeFrom(String value) {
        return new FileExtension(value);
    }

    public static FileExtension checkAndMakeFrom(String value) throws Exception {
        final var cleanedValue = value.trim().toLowerCase();

        Assert.notNull(cleanedValue, EmptyFileExtensionException::new);
        Assert.notBlank(cleanedValue, EmptyFileExtensionException::new);

        return new FileExtension(cleanedValue);
    }

    @Override
    public String toString() {
        return value;
    }

    public abstract static sealed class Exception extends DomainException permits EmptyFileExtensionException {
        protected Exception(String message) {
            super(message);
        }
    }

    public static final class EmptyFileExtensionException extends Exception {
        public EmptyFileExtensionException() {
            super("File extension is required");
        }

        @Override
        public String getCode() {
            return "EmptyFileExtension";
        }
    }
}
