package com.azat4dev.booking.users.config.users_commands.domain;

import com.azat4dev.booking.shared.config.domain.AutoConnectCommandHandlersToBus;
import com.azat4dev.booking.shared.domain.interfaces.tracing.ExtractTraceContext;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsReader;
import com.azat4dev.booking.shared.domain.values.user.UserIdFactory;
import com.azat4dev.booking.shared.domain.values.user.UserIdFactoryImpl;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.commands.domain.handlers.users.Users;
import com.azat4dev.booking.users.commands.domain.handlers.users.UsersImpl;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@AutoConnectCommandHandlersToBus
@Configuration
public class DomainConfig {

    private final TimeProvider timeProvider;
    private final ExtractTraceContext extractTraceContext;
    private final UnitOfWorkFactory unitOfWorkFactory;

    @Bean
    public UserIdFactory userIdFactory() {
        return new UserIdFactoryImpl();
    }

    @Bean
    public Users usersService(OutboxEventsReader outboxEventsReader) {
        return new UsersImpl(
            timeProvider,
            outboxEventsReader::trigger,
            unitOfWorkFactory,
            extractTraceContext
        );
    }
}
