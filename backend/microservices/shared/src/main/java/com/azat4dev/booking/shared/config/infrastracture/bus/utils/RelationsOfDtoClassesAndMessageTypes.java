package com.azat4dev.booking.shared.config.infrastracture.bus.utils;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class RelationsOfDtoClassesAndMessageTypes {

    private final Map<String, Class<?>> dtoClassesByMessageTypes;
    private final Map<Class<?>, String> messageTypesByDtoClasses;

    public RelationsOfDtoClassesAndMessageTypes(Item... items) {

        dtoClassesByMessageTypes = new HashMap<>();
        messageTypesByDtoClasses = new HashMap<>();

        Arrays.stream(items)
            .forEach(this::add);
    }

    @Nonnull
    public Class<?> getDtoClass(String messageType) throws Exception.MessageTypeNotFound {
        final var dtoClass = dtoClassesByMessageTypes.get(messageType);
        if (dtoClass == null) {
            throw new Exception.MessageTypeNotFound(messageType);
        }

        return dtoClass;
    }

    @Nonnull
    public String getMessageType(Class<?> dtoClass) throws Exception.MessageTypeNotFound {
        final var messageType = messageTypesByDtoClasses.get(dtoClass);
        if (messageType == null) {
            throw new Exception.DtoClassNotFound(dtoClass);
        }
        return messageType;
    }

    protected List<Item> getItems() {
        return dtoClassesByMessageTypes.entrySet().stream()
            .map(entry -> new Item(entry.getKey(), entry.getValue()))
            .toList();
    }

    protected void add(Item item) {

        final var messageType = item.messageType();
        final var dtoClass = item.dtoClass();

        final var prevClass = dtoClassesByMessageTypes.put(messageType, dtoClass);
        if (prevClass != null) {
            throw new Exception.MessageTypeDuplicated(messageType, dtoClass, prevClass);
        }

        final var prevMessageType = messageTypesByDtoClasses.put(dtoClass, messageType);
        if (prevMessageType != null) {
            throw new Exception.DtoClassDuplicated(dtoClass, messageType, prevMessageType);
        }
    }

    // Types

    public record Item(String messageType, Class<?> dtoClass) {
    }

    /**
     * Exceptions for {@link RelationsOfDtoClassesAndMessageTypes}
     */
    public abstract static class Exception extends RuntimeException {
        protected Exception(String message) {
            super(message);
        }

        @Getter
        public static final class MessageTypeNotFound extends Exception {

            private final String messageType;

            public MessageTypeNotFound(String messageType) {
                super("Message type not found: " + messageType);
                this.messageType = messageType;
            }
        }

        @Getter
        public static final class DtoClassNotFound extends Exception {

            private final Class<?> dtoClass;

            public DtoClassNotFound(Class<?> dtoClass) {
                super("Dto class not found: " + dtoClass);
                this.dtoClass = dtoClass;
            }
        }

        @Getter
        public static final class DtoClassDuplicated extends Exception {

            private final Class<?> dtoClass;

            private final String messageType1;

            private final String messageType2;

            public DtoClassDuplicated(Class<?> dtoClass, String messageType1, String messageType2) {
                super(String.format("Dto class duplicated: class=%s, messageType1=%s, messageType2=%s",
                    dtoClass, messageType1, messageType2));
                this.dtoClass = dtoClass;
                this.messageType1 = messageType1;
                this.messageType2 = messageType2;
            }
        }

        @Getter
        public static final class MessageTypeDuplicated extends Exception {

            private final String messageType;

            private final Class<?> dtoClass1;

            private final Class<?> dtoClass2;

            public MessageTypeDuplicated(String messageType, Class<?> dtoClass1, Class<?> dtoClass2) {
                super(String.format("Message type duplicated: messageType=%s, dtoClass1=%s, dtoClass2=%s",
                    messageType, dtoClass1, dtoClass2));
                this.messageType = messageType;
                this.dtoClass1 = dtoClass1;
                this.dtoClass2 = dtoClass2;
            }
        }
    }
}
