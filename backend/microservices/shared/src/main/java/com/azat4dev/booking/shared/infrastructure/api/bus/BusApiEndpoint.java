package com.azat4dev.booking.shared.infrastructure.api.bus;

import lombok.Getter;

import java.util.Optional;

public interface BusApiEndpoint<T> {

    void handle(
        InputMessage<T> message,
        Reply reply
    );

    void handleException(
        Throwable exception,
        Reply reply
    );

    String getInputAddress();

    Optional<String> getReplyAddress();

    MessageInfo getInputMessageInfo();

    MessageInfo[] getResponseMessagesInfo();

    record MessageInfo(
        String messageType,
        Class<?> messageClass
    ) {
    }

    interface Reply {
        void publish(
            Optional<String> partitionKey,
            Object response
        ) throws Exception.EndpointDoesntHaveReplyAddress;

        void publish(
            Object response
        ) throws Exception.EndpointDoesntHaveReplyAddress;


        // Exceptions

        abstract class Exception extends RuntimeException {
            protected Exception(String message) {
                super(message);
            }

            @Getter
            public static final class EndpointDoesntHaveReplyAddress extends Reply.Exception {

                private final Class<? extends BusApiEndpoint> endpointClass;

                public EndpointDoesntHaveReplyAddress(Class<? extends BusApiEndpoint> endpointClass) {
                    super(String.format("There is no reply address for: endpoint=%s", endpointClass.getSimpleName()));
                    this.endpointClass = endpointClass;
                }
            }
        }
    }
}