package com.azat4dev.booking.shared.infrastructure.bus;

import com.azat4dev.booking.shared.infrastructure.serializers.MapAnyDomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessageSerializerJSONWithDomainEvents implements MessageSerializer<String> {

    private final MessageSerializer<String> messageSerializer;
    private final MapAnyDomainEvent mapDomainEvent;

    @Override
    public Object deserialize(String serializedMessage, String messageType) {

        final var dto = messageSerializer.deserialize(serializedMessage, messageType);
        if (mapDomainEvent.isSupportedDTO(dto.getClass())) {
            return mapDomainEvent.fromDTO(dto);
        }

        return dto;
    }

    @Override
    public <M> String serialize(M message) {

        if (message instanceof DomainEventPayload inst) {
            final var dto = mapDomainEvent.toDTO(inst);
            return messageSerializer.serialize(dto);
        }

        return messageSerializer.serialize(message);
    }
}
