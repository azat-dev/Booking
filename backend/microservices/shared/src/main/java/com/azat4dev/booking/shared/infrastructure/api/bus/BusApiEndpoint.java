package com.azat4dev.booking.shared.infrastructure.api.bus;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

public interface BusApiEndpoint<T> {

    void handle(
        Request<T> request,
        Reply reply
    );

    default void handleException(
        Throwable exception,
        Request<T> request,
        Reply reply
    ) {

    };

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