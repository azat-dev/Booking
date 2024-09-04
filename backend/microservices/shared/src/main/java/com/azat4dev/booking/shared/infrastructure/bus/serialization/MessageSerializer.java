package com.azat4dev.booking.shared.infrastructure.bus.serialization;


import com.azat4dev.booking.shared.infrastructure.bus.Message;

public interface MessageSerializer {

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
    }
}