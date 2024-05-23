package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.shared.domain.event.DomainEventPayload;

import java.io.Serializable;


public interface DomainEventPayloadDTO extends Serializable {

    DomainEventPayload toDomain();
}
