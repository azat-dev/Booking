package com.azat4dev.booking.shared.data;

import com.azat4dev.booking.shared.domain.event.DomainEventPayload;

import java.io.Serializable;

public interface DomainEventPayloadDTO extends Serializable {

    DomainEventPayload toDomain();
}
