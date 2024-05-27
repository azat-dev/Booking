package com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories;

public interface UnitOfWork {

    void save();

    void rollback();

    OutboxEventsRepository getOutboxEventsRepository();

    ListingsRepository getUsersRepository();
}
