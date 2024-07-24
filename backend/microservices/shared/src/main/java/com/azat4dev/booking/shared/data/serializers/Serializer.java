package com.azat4dev.booking.shared.data.serializers;

public interface Serializer<P, K> {

    K serialize(P obj);

    P deserialize(K serializedData);

    Class<P> getOriginalClass();

    Class<K> getSerializedClass();

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
