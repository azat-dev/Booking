package com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories;

@FunctionalInterface
public interface UnitOfWorkFactory {

    UnitOfWork make();
}
