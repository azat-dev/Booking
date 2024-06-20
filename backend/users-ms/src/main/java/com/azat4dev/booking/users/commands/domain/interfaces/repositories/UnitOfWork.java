package com.azat4dev.booking.users.commands.domain.interfaces.repositories;

import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;


public interface UnitOfWork {

    OutboxEventsRepository getOutboxEventsRepository();

    UsersRepository getUsersRepository();

    <T> T doOrFail(Action<T> action) throws Exception;

    @FunctionalInterface
    interface Action<T> {
        T run() throws Exception;
    }
}
