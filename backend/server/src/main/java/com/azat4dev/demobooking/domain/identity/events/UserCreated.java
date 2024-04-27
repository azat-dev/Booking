package com.azat4dev.demobooking.domain.identity.events;

import com.azat4dev.demobooking.domain.common.CommandId;
import com.azat4dev.demobooking.domain.common.DomainEvent;

public final class UserCreated extends DomainEvent {

    private UserCreatedPayload payload;
    private CommandId sourceCommandId;

    public UserCreated(
            CommandId sourceCommandId,
            long timestamp,
            UserCreatedPayload payload
    ) {
        super(timestamp);
        if (sourceCommandId == null) {
            throw new NullPointerException("sourceCommandId");
        }
        if (payload == null) {
            throw new NullPointerException("payload");
        }

        this.payload = payload;
        this.sourceCommandId = sourceCommandId;
    }

    @Override
    public String getType() {
        return "UserCreated";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public UserCreatedPayload getPayload() {
        return this.payload;
    }

    public CommandId getSourceCommandId() {
        return sourceCommandId;
    }
}
