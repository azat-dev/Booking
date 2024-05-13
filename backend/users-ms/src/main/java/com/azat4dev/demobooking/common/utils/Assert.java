package com.azat4dev.demobooking.common.utils;

import com.azat4dev.demobooking.common.domain.DomainException;
import org.springframework.util.StringUtils;

import java.util.function.Function;
import java.util.function.Supplier;

public class Assert {

    public static <E extends DomainException> StringValidator<E> string(String value, ExceptionSupplier<E> exceptionSupplier) {
        if (exceptionSupplier == null) {
            throw new IllegalArgumentException("Exception supplier is null");
        }

        return new StringValidator<E>(value, exceptionSupplier);
    }

    public static <E extends Exception> void notNull(Object object, ExceptionSupplier<E> exceptionProducer) throws E {
        if (object == null) {
            throw exceptionProducer.get();
        }
    }

    public static <E extends Exception> void notBlank(String value, ExceptionSupplier<E> exceptionProducer) throws E {
        if (!StringUtils.hasText(value)) {
            throw exceptionProducer.get();
        }
    }

    public static <E extends Exception> void isTrue(boolean value, ExceptionSupplier<E> exceptionProducer) throws E {
        if (!value) {
            throw exceptionProducer.get();
        }
    }

    public static <E extends Exception> void hasPattern(String value, String pattern, ExceptionSupplier<E> exceptionProducer) throws E {
        if (!value.matches(pattern)) {
            throw exceptionProducer.get();
        }
    }

    public interface ExceptionSupplier<E extends Exception> extends Supplier<E> {
    }

    public static class StringValidator<E extends Exception> {

        private final String value;
        private final ExceptionSupplier<E> exceptionSupplier;

        protected StringValidator(String value, ExceptionSupplier<E> exceptionSupplier) {
            this.value = value;
            this.exceptionSupplier = exceptionSupplier;
        }

        public StringValidator<E> notNull() throws E {
            Assert.notNull(value, exceptionSupplier);
            return this;
        }

        public StringValidator<E> notBlank() throws E {
            Assert.notBlank(value, exceptionSupplier);
            return this;
        }

        public StringValidator<E> minLength(int minLength) throws E {
            Assert.isTrue(value.length() >= minLength, exceptionSupplier);
            return this;
        }

        public StringValidator<E> maxLength(int maxLength) throws E {
            Assert.isTrue(value.length() <= maxLength, exceptionSupplier);
            return this;
        }

        public StringValidator<E> hasPattern(String pattern) throws E {
            Assert.hasPattern(value, pattern, exceptionSupplier);
            return this;
        }

        public StringValidator<E> isTrue(Function<String, Boolean> predicate) throws E {
            Assert.isTrue(predicate.apply(value), exceptionSupplier);
            return this;
        }
    }
}
