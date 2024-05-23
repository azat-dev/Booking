package com.azat4dev.booking.users.users_commands.domain.interfaces.repositories;

@FunctionalInterface
public interface UnitOfWorkFactory {

    UnitOfWork make();
}
