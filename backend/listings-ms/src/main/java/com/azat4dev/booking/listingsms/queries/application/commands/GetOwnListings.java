package com.azat4dev.booking.listingsms.queries.application.commands;

import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.shared.application.events.ApplicationCommand;

public record GetOwnListings(
    HostId hostId
) implements ApplicationCommand {
}
