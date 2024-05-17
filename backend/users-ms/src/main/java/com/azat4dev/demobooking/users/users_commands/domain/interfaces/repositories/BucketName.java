package com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;
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
        Assert.notNull(value, Exception.WrongFormat::new);
        Assert.notBlank(value, Exception.WrongFormat::new);
        Assert.hasPattern(value, BUCKET_NAME_REGEX, Exception.WrongFormat::new);

        return new BucketName(value);
    }

    @Override
    public String toString() {
        return value;
    }

    public static abstract class Exception extends DomainException {
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
