package com.azat4dev.booking.listingsms.commands.domain.values;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Optional;

@EqualsAndHashCode
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionalField<T> {

    private final Optional<T> value;

    public static <T> OptionalField<T> missed() {
        return new OptionalField<>(Optional.empty());
    }

    public static <T> OptionalField<T> present(T value) {
        return new OptionalField<>(Optional.of(value));
    }

    public static <T> OptionalField<T> from(JsonNullable<T> value) {
        if (!value.isPresent()) {
            return OptionalField.missed();
        }
        return new OptionalField<>(Optional.of(value.get()));
    }

    public static <T> OptionalField<Optional<T>> fromNullable(JsonNullable<T> value) {
        if (!value.isPresent()) {
            return OptionalField.missed();
        }

        return OptionalField.present(Optional.of(value.get()));
    }

    public static <T, Output> OptionalField<Optional<Output>> fromNullable(JsonNullable<T> value, MapValue<T, Output, RuntimeException> mapper) {
        if (!value.isPresent()) {
            return OptionalField.missed();
        }

        final var mappedValue = mapper.map(value.get());
        return OptionalField.present(Optional.of(mappedValue));
    }

    public boolean isMissed() {
        return value.isEmpty();
    }

    public T get() {
        if (isMissed()) {
            throw new IllegalStateException("Value is undefined");
        }

        return value.get();
    }

    public Optional<T> getOptional() {
        if (isMissed()) {
            return Optional.empty();
        }

        return value;
    }

    public <E extends Exception> void ifPresent(PresentAction<T, E> action) throws E {
        if (isMissed()) {
            return;
        }

        action.run(value.get());
    }

    public <Output, E extends Exception> OptionalField<Output> map(MapValue<T, Output, E> mapper) throws E {
        if (isMissed()) {
            return OptionalField.missed();
        }

        final var mappedValue = mapper.map(value.get());
        return OptionalField.present(mappedValue);
    }

    @FunctionalInterface
    public interface MapValue<Input, Output, Ex extends Exception> {
        Output map(Input value) throws Ex;
    }

    @FunctionalInterface
    public interface PresentAction<Input, Ex extends Exception> {
        void run(Input value) throws Ex;
    }
}