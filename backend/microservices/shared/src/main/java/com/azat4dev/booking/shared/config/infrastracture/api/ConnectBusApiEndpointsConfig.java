package com.azat4dev.booking.shared.config.infrastracture.api;

import com.azat4dev.booking.shared.infrastructure.api.bus.BusApiEndpoint;
import com.azat4dev.booking.shared.infrastructure.api.bus.InputMessage;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Configuration
public class ConnectBusApiEndpointsConfig {

    private final List<Closeable> cancellations = new LinkedList<>();
    private final ApplicationContext applicationContext;
    private final MessageBus<?> messageBus;

    private static Map<String, List<BusApiEndpoint>> groupEndpointsByAddress(List<BusApiEndpoint> endpoints) {
        return endpoints.stream()
            .collect(
                Collectors.groupingBy(
                    BusApiEndpoint::getInputAddress,
                    Collectors.toList()
                )
            );
    }

    private List<BusApiEndpoint> findEndpoints(ApplicationContext context) {

        return Arrays.stream(context.getBeanNamesForType(BusApiEndpoint.class))
            .map(name -> (BusApiEndpoint) context.getBean(name))
            .toList();
    }

    @PostConstruct
    public void connectCommandHandlersToBus() {

        final var endpoints = findEndpoints(applicationContext);
        final var groupedEndpoints = groupEndpointsByAddress(endpoints);

        for (var entry : groupedEndpoints.entrySet()) {
            final var inputAddress = entry.getKey();
            final var items = entry.getValue();

            for (var endpoint : items) {
                final var cancellation = messageBus.listen(
                    inputAddress,
                    msg -> {
                        final var message = (MessageBus.ReceivedMessage) msg;

                        try {
                            log.atDebug()
                                .addArgument(() -> endpoint.getClass().getSimpleName())
                                .addArgument(inputAddress)
                                .addKeyValue("message.type", inputAddress)
                                .addKeyValue("message.id", message::messageId)
                                .addKeyValue("message.issuedAt", message::messageSentAt)
                                .log("Pass event into bus api endpoint: endpoint={} inputAddress={}");

                            endpoint.handle(
                                new InputMessage<>(
                                    message.messageId(),
                                    message.messageType(),
                                    message.messageSentAt(),
                                    message.payload()
                                ),
                                messageBus
                            );
                        } catch (Exception e) {
                            log.atDebug()
                                .setCause(e)
                                .addArgument(inputAddress)
                                .addKeyValue("message.type", inputAddress)
                                .addKeyValue("message.id", message::messageId)
                                .addKeyValue("message.issuedAt", message::messageSentAt)
                                .log("Exception during handling message by endpoint: {}");
                            throw new FailedToExecuteBusApiEndpoint(e);
                        }
                    });
                cancellations.add(cancellation);

                log.atInfo()
                    .addArgument(() -> ClassUtils.getUserClass(endpoint).getSimpleName())
                    .addArgument(inputAddress)
                    .log("Connected bus endpoint: endpoint={} inputAddress={}");
            }
        }
    }

    @PreDestroy
    public void disconnectHandlersFromBus() {
        cancellations.forEach(cancellation -> {
            try {
                cancellation.close();
            } catch (IOException e) {
                log.atError()
                    .setCause(e)
                    .log("Can't close cancellation");
            }
        });

        log.atInfo()
            .log("Disconnected endpoints from the bus");
    }

    public static class FailedToExecuteBusApiEndpoint extends RuntimeException {
        public FailedToExecuteBusApiEndpoint(Throwable e) {
            super(e);
        }
    }
}
