package com.azat4dev.booking.shared.infrastructure.serializers;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

public interface MapAnyDomainEvent {
    <DOMAIN extends DomainEventPayload, DTO> DTO toDTO(DOMAIN domain);

    <DTO> DomainEventPayload fromDTO(DTO dto);

    boolean isSupportedDTO(Class<?> dtoClass);

    // Exceptions

    abstract class Exception extends RuntimeException {
        protected Exception(String message) {
            super(message);
        }

        public static final class FailedSerialize extends Exception {
            public FailedSerialize(String message) {
                super(message);
            }
        }

        public static final class FailedDeserialize extends Exception {
            public FailedDeserialize(String message) {
                super(message);
            }
        }
    }
}