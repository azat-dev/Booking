package com.azat4dev.demobooking.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

public abstract class DomainEvent<Payload extends Serializable> implements Serializable {

    @Getter
    protected EventId id;

    @Getter
    @Setter
    protected long timestampMs;

    public DomainEvent() {
    }

    public DomainEvent(
        EventId id,
        long timestampMs
    ) {
        this.id = id;
        this.timestampMs = timestampMs;
    }

    public abstract String getType();

    public abstract int getVersion();

    public abstract Payload getPayload();

}
