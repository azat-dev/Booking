package com.azat4dev.booking.shared.data.bus;


import com.azat4dev.booking.shared.data.serializers.TypedSerializer;

public interface MessageSerializer<S> {

    Object deserialize(S serializedMessage, String messageType) throws Exception;

    <M> S serialize(M message) throws Exception;

    // Exceptions

    abstract class Exception extends RuntimeException {
        protected Exception(Throwable cause) {
            super(cause);
        }

        public static final class FailedSerialize extends TypedSerializer.Exception {
            public FailedSerialize(Throwable cause) {
                super(cause);
            }
        }

        public static final class FailedDeserialize extends TypedSerializer.Exception {
            public FailedDeserialize(Throwable cause) {
                super(cause);
            }
        }
    }
}