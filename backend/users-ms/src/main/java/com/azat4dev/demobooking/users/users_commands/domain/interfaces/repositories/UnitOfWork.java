package com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories;

public interface UnitOfWork {

    void save();

    void rollback();

    OutboxEventsRepository getOutboxEventsRepository();

    UsersRepository getUsersRepository();
}
