package com.azat4dev.booking.shared.domain;

import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.events.EventId;

import java.time.LocalDateTime;

public interface CommandHandler<C extends Command> {

    void handle(C command, EventId eventId, LocalDateTime issuedAt) throws DomainException;
}
