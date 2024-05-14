package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.common.domain.CommandHandler;
import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.common.domain.event.DomainEventsFactory;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.UpdateUserPhoto;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.FilesRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UpdateUserPhotoHandler implements CommandHandler<DomainEventNew<UpdateUserPhoto>> {

    private final FilesRepository filesRepository;
    private final DomainEventsBus domainEventsBus;
    private final DomainEventsFactory eventsFactory;

    @Override
    public void handle(DomainEventNew<UpdateUserPhoto> command) {

        final var payload = command.payload();

    }
}
