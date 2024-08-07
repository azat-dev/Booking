package com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories;

import com.azat4dev.booking.shared.infrastructure.persistence.repositories.outbox.OutboxEventsRepository;

public interface UnitOfWork {

    void save();

    void rollback();

    OutboxEventsRepository getOutboxEventsRepository();

    ListingsRepository getListingsRepository();
}
