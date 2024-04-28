package com.azat4dev.demobooking.common;

public abstract class DomainEvent {

    protected long timestampMs;

    public DomainEvent() {
    }

    public DomainEvent(long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public long getTimestampMillis() {
        return timestampMs;
    }

    public void setTimestampMillis(long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public abstract  String getType();

    public abstract int getVersion();

    public abstract  Object getPayload();
}
