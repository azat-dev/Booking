package com.azat4dev.booking.shared.data.serializers;

public interface Mapper<P, K> {

    K toDTO(P domain);

    P toDomain(K dto);

    Class<P> getDomainClass();

    Class<K> getDTOClass();
}
