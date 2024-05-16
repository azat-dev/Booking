package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;


public interface DomainEventPayloadDTO extends Serializable {

    DomainEventPayload toDomain();
}
