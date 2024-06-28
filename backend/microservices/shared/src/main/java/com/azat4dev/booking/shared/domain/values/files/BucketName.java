package com.azat4dev.booking.shared.domain.values.files;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.utils.Assert;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class BucketName {

    public static final String BUCKET_NAME_REGEX = "^[a-z0-9][a-z0-9-]+$";

    private final String value;

    private BucketName(String value) {
        this.value = value;
    }

    public static BucketName makeWithoutChecks(String value) {
        return new BucketName(value);
    }

    public static BucketName checkAndMake(String value) throws Exception {

        final var cleanedValue = value.toLowerCase();
        Assert.notNull(cleanedValue, Exception.WrongFormat::new);
        Assert.notBlank(cleanedValue, Exception.WrongFormat::new);
        Assert.hasPattern(cleanedValue, BUCKET_NAME_REGEX, Exception.WrongFormat::new);

        return new BucketName(cleanedValue);
    }

    @Override
    public String toString() {
        return value;
    }

    public abstract static class Exception extends DomainException {
        Exception(String message) {
            super(message);
        }

        public static final class WrongFormat extends Exception {
            public WrongFormat() {
                super("BucketName has wrong format");
            }
        }
    }
}
