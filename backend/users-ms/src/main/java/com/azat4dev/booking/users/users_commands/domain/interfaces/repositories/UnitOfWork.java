package com.azat4dev.booking.users.users_commands.domain.interfaces.repositories;

import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;

public interface UnitOfWork {

    void save();

    void rollback();

    OutboxEventsRepository getOutboxEventsRepository();

    UsersRepository getUsersRepository();
}
