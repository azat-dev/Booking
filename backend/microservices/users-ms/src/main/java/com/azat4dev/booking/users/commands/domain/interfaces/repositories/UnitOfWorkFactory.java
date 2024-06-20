package com.azat4dev.booking.users.commands.domain.interfaces.repositories;

@FunctionalInterface
public interface UnitOfWorkFactory {

    UnitOfWork make();
}
