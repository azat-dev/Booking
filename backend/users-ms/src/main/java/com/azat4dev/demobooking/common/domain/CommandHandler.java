package com.azat4dev.demobooking.common.domain;

import com.azat4dev.demobooking.common.domain.event.Command;
import com.azat4dev.demobooking.common.domain.event.EventId;

import java.time.LocalDateTime;

public interface CommandHandler<CMD extends Command> {

    void handle(CMD command, EventId eventId, LocalDateTime issuedAt) throws DomainException;
}
