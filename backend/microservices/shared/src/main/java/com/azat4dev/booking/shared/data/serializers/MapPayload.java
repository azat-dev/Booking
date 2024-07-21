package com.azat4dev.booking.shared.data.serializers;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

public interface MapPayload<P extends DomainEventPayload, K> extends Mapper<P, K> {

    K toDTO(P domain);

    P toDomain(K dto);

    Class<P> getDomainClass();

    Class<K> getDTOClass();
}
