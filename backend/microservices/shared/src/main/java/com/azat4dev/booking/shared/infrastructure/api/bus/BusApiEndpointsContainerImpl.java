package com.azat4dev.booking.shared.infrastructure.api.bus;

import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import io.micrometer.observation.annotation.Observed;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.util.List;
import java.util.Optional;

@Slf4j
@Observed
public class BusApiEndpointsContainerImpl implements BusApiEndpointsContainer {

    private final List<Closeable> cancellations;

    public BusApiEndpointsContainerImpl(
        List<BusApiEndpoint<?>> endpoints,
        MessageBus messageBus,
        GetMessageTypeForDtoClass getMessageTypeForDtoClass,
        GenerateMessageId generateMessageId
    ) {
        cancellations = endpoints.stream()
            .map(endpoint -> {
                return connect(
                    messageBus,
                    endpoint,
                    endpoint.getInputAddress(),
                    getMessageTypeForDtoClass,
                    generateMessageId
                );
            })
            .toList();
    }

    private static Closeable connect(
        MessageBus messageBus,
        BusApiEndpoint endpoint,
        String inputAddress,
        GetMessageTypeForDtoClass getMessageTypeForDtoClass,
        GenerateMessageId generateMessageId
    ) {

        return messageBus.listen(
            inputAddress,
            msg -> {
                final var message = (MessageBus.ReceivedMessage) msg;

                log.atDebug()
                    .addArgument(() -> endpoint.getClass().getSimpleName())
                    .addArgument(inputAddress)
                    .addKeyValue("message.type", inputAddress)
                    .addKeyValue("message.id", message::messageId)
                    .addKeyValue("message.issuedAt", message::messageSentAt)
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

                        messageBus.publish(
                            replyAddress.get(),
                            partitionKey,
                            generateMessageId.run(),
                            getMessageTypeForDtoClass.run(response.getClass()),
                            response,
                            Optional.empty(),
                            Optional.of(message.messageId())
                        );
                    }

                    @Override
                    public void publish(Object response) {
                        publish(Optional.empty(), response);
                    }
                };

                final var request = new Request<>(
                    message.messageId(),
                    message.messageType(),
                    message.messageSentAt(),
                    replyAddress,
                    message.payload()
                );

                try {

                    endpoint.handle(request, reply);

                } catch (Throwable e) {
                    log.atError()
                        .setCause(e)
                        .addArgument(inputAddress)
                        .addKeyValue("message.type", inputAddress)
                        .addKeyValue("message.id", message::messageId)
                        .addKeyValue("message.issuedAt", message::messageSentAt)
                        .log("Exception during handling message by endpoint: {}");

                    endpoint.handleException(e, request, reply);
                }
            });
    }

    @Override
    public void close() {
        cancellations.forEach(cancellation -> {
            try {
                cancellation.close();
            } catch (Exception e) {
                log.atError()
                    .setCause(e)
                    .log("Can't close cancellation");
            }
        });
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
