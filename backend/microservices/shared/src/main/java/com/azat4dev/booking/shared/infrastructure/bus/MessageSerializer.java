package com.azat4dev.booking.shared.infrastructure.bus;


public interface MessageSerializer {

    Message deserialize(byte[] serializedMessage) throws Exception.FailedDeserialize;

    byte[] serialize(Message message) throws Exception.FailedSerialize;

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