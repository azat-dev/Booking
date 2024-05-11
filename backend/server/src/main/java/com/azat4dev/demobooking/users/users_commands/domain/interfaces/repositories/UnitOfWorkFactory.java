package com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories;

@FunctionalInterface
public interface UnitOfWorkFactory {

    UnitOfWork make();
}
