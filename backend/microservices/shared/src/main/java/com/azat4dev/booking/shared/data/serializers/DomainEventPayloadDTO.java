package com.azat4dev.booking.shared.data.serializers;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

import java.io.Serializable;

public interface DomainEventPayloadDTO extends Serializable {

    DomainEventPayload toDomain();
}
