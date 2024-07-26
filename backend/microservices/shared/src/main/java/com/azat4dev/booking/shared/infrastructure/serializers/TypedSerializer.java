package com.azat4dev.booking.shared.infrastructure.serializers;

public interface TypedSerializer<SERIALIZED> {

    <DATA> SERIALIZED serialize(DATA data);

    <DATA> DATA deserialize(SERIALIZED serializedData, Class<DATA> type);

    // Exceptions

    abstract class Exception extends RuntimeException {
        protected Exception(Throwable cause) {
            super(cause);
        }

        public static final class FailedSerialize extends Exception {
            public FailedSerialize(Throwable cause) {
                super(cause);
            }
        }

        public static final class FailedDeserialize extends Exception {
            public FailedDeserialize(Throwable cause) {
                super(cause);
            }
        }
    }
}
