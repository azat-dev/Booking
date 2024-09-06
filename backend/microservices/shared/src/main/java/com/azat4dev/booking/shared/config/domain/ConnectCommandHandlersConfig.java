package com.azat4dev.booking.shared.config.domain;

import com.azat4dev.booking.shared.domain.CommandHandler;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
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
public class ConnectCommandHandlersConfig {

//    private final List<Closeable> cancellations = new LinkedList<>();
//    private final ApplicationContext applicationContext;
//    private final DomainEventsBus domainEventsBus;
//
//
//    private static Map<Class<Command>, List<CommandHandler<Command>>> groupHandlersByPayload(List<CommandHandler<Command>> handlers) {
//        return handlers.stream()
//            .collect(
//                Collectors.groupingBy(
//                    CommandHandler::getCommandClass,
//                    Collectors.toList()
//                )
//            );
//    }
//
//    public static List<CommandHandler<Command>> findCommandHandlers(ApplicationContext context) {
//
//        return Arrays.stream(context.getBeanNamesForType(CommandHandler.class))
//            .map(name -> (CommandHandler<Command>) context.getBean(name))
//            .toList();
//    }
//
//    @PostConstruct
//    public void connectCommandHandlersToBus() {
//
//        final var commandHandlers = findCommandHandlers(applicationContext);
//        final var groupedHandlers = groupHandlersByPayload(commandHandlers);
//
//        for (var entry : groupedHandlers.entrySet()) {
//            final var eventType = entry.getKey();
//            final var handlers = entry.getValue();
//
//            for (var handler : handlers) {
//                final var cancellation = domainEventsBus.listen(
//                    eventType,
//                    event -> {
//                        try {
//                            log.atDebug()
//                                .addArgument(eventType)
//                                .addKeyValue("event.type", eventType)
//                                .addKeyValue("event.id", event::id)
//                                .addKeyValue("event.issuedAt", event::issuedAt)
//                                .addKeyValue("event.payload", () -> event.payload().toString())
//                                .log("Pass event into command handler: {}");
//
//                            handler.handle(event.payload(), event.id(), event.issuedAt());
//                        } catch (DomainException e) {
//                            log.atDebug()
//                                .setCause(e)
//                                .addArgument(eventType)
//                                .addKeyValue("event.type", eventType)
//                                .addKeyValue("event.id", event::id)
//                                .addKeyValue("event.issuedAt", event::issuedAt)
//                                .addKeyValue("event.payload", () -> event.payload().toString())
//                                .log("Exception during handling by command handler: {}");
//                            throw new FailedToExecuteCommandHandler(e);
//                        }
//                    });
//                cancellations.add(cancellation);
//
//                log.atInfo()
//                    .addArgument(() -> ClassUtils.getUserClass(handler).getSimpleName())
//                    .addArgument(eventType)
//                    .log("Connected command handler {} to the bus {}");
//            }
//        }
//    }
//
//    @PreDestroy
//    public void disconnectHandlersFromBus() {
//        cancellations.forEach(cancellation -> {
//            try {
//                cancellation.close();
//            } catch (IOException e) {
//                log.atError()
//                    .setCause(e)
//                    .log("Can't close cancellation");
//            }
//        });
//
//        log.atInfo()
//            .log("Disconnected command handlers from the bus");
//    }
//
//    public static class FailedToExecuteCommandHandler extends RuntimeException {
//        public FailedToExecuteCommandHandler(Throwable e) {
//            super(e);
//        }
//    }
}
