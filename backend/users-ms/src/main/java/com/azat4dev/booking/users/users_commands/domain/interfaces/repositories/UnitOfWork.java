package com.azat4dev.booking.users.users_commands.domain.interfaces.repositories;

public interface UnitOfWork {

    void save();

    void rollback();

    OutboxEventsRepository getOutboxEventsRepository();

    UsersRepository getUsersRepository();
}
