package com.azat4dev.booking.shared.infrastructure.bus;

import com.azat4dev.booking.shared.infrastructure.api.bus.BusApiEndpoint;
import com.azat4dev.booking.shared.infrastructure.api.bus.Request;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
public class MessageListenerForBusApiEndpoint implements MessageListener {

    private final BusApiEndpoint endpoint;
    private Supplier<MessageBus> getMessageBus;
    private final GenerateMessageId generateMessageId;
    private final GetMessageTypeForDtoClass getMessageTypeForDtoClass;

    @Override
    public void consume(Message message) {

        if (!endpoint.isMessageTypeAllowed(message.type())) {
            return;
        }

        log.atDebug()
            .addArgument(() -> endpoint.getClass().getSimpleName())
            .addArgument(endpoint::getInputAddress)
            .addKeyValue("message.type", message::type)
            .addKeyValue("message.id", message::id)
            .addKeyValue("message.issuedAt", message::sentAt)
            .log("Pass event into bus api endpoint: endpoint={} inputAddress={}");

        final Optional<String> replyAddress = endpoint.hasDynamicReplyAddress() ? message.replyTo() : endpoint.getStaticReplyAddress();

        final var reply = new BusApiEndpoint.Reply() {
            @Override
            public void publish(Optional<String> partitionKey, Object response) {

                if (replyAddress.isEmpty()) {
                    log.atError()
                        .addArgument(endpoint.getClass().getSimpleName())
                        .log("There is no reply address for: endpoint={}");
                    throw new Exception.EndpointDoesntHaveReplyAddress(endpoint.getClass());
                }

                final var messageBus = getMessageBus.get();

                messageBus.publish(
                    replyAddress.get(),
                    partitionKey,
                    MessageBus.Data.with(
                        generateMessageId.run(),
                        getMessageTypeForDtoClass.run(response.getClass()),
                        Optional.of(message.id()),
                        Optional.empty(),
                        response
                    )
                );
            }

            @Override
            public void publish(Object response) {
                publish(Optional.empty(), response);
            }
        };

        final var request = new Request<>(
            message.id(),
            message.type(),
            message.sentAt(),
            replyAddress,
            message.payload()
        );

        try {

            endpoint.handle(request, reply);

        } catch (Throwable e) {
            log.atError()
                .setCause(e)
                .addArgument(endpoint::getInputAddress)
                .addKeyValue("message.type", message::type)
                .addKeyValue("message.id", message::id)
                .addKeyValue("message.issuedAt", message::sentAt)
                .log("Exception during handling message by endpoint: {}");

            endpoint.handleException(e, request, reply);
        }
    }

    @FunctionalInterface
    public interface GetMessageTypeForDtoClass {

        @Nonnull
        String run(Class<?> messageClass);
    }

    @FunctionalInterface
    public interface GenerateMessageId {
        @Nonnull
        String run();
    }
}