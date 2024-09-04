package com.azat4dev.booking.shared.infrastructure.bus.serialization;


import com.azat4dev.booking.shared.infrastructure.bus.Message;

public interface MessageDeserializer {

    Message deserialize(byte[] serializedMessage) throws Exception.FailedDeserialize;

    abstract class Exception extends RuntimeException {
        protected Exception(Throwable cause) {
            super(cause);
        }

        public static final class FailedDeserialize extends Exception {
            public FailedDeserialize(Throwable cause) {
                super(cause);
            }
        }
    }
}