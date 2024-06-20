package com.azat4dev.booking.listingsms.commands.domain.values;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@EqualsAndHashCode(of = "value")
@Getter
public final class HostId {

    private final UUID value;

    private HostId(UUID value) {
        this.value = value;
    }

    public static HostId checkAndMakeFrom(String value) {
        return new HostId(UUID.fromString(value));
    }

    public static HostId dangerouslyMakeFrom(String value) {
        return new HostId(UUID.fromString(value));
    }

    public String toString() {
        return value.toString();
    }

    public static HostId fromUserId(UserId userId) {
        return new HostId(userId.value());
    }
}