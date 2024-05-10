package com.azat4dev.demobooking.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public abstract class DomainEvent implements Serializable {

    @Getter
    protected String id;

    @Getter
    @Setter
    protected long timestampMs;

    public DomainEvent() {
    }

    public DomainEvent(
        String id,
        long timestampMs
    ) {
        this.id = id;
        this.timestampMs = timestampMs;
    }

    public abstract String getType();

    public abstract int getVersion();

    public abstract Object getPayload();

}
