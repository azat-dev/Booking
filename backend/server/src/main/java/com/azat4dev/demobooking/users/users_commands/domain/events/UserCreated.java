package com.azat4dev.demobooking.users.users_commands.domain.events;

import com.azat4dev.demobooking.common.CommandId;
import com.azat4dev.demobooking.common.DomainEvent;

public final class UserCreated extends DomainEvent {

    private final UserCreatedPayload payload;

    public UserCreated(
        String eventId,
        long timestamp,
        UserCreatedPayload payload
    ) {
        super(eventId, timestamp);

        this.payload = payload;
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
}
