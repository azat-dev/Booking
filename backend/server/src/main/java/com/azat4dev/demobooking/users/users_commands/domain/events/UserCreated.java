package com.azat4dev.demobooking.users.users_commands.domain.events;

import com.azat4dev.demobooking.common.CommandId;
import com.azat4dev.demobooking.common.DomainEvent;

public final class UserCreated extends DomainEvent {

    private final UserCreatedPayload payload;
    private final CommandId sourceCommandId;

    public UserCreated(
        CommandId sourceCommandId,
        long timestamp,
        UserCreatedPayload payload
    ) {
        super(timestamp);

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
