package com.azat4dev.demobooking.users.users_commands.domain.handlers.users;

import com.azat4dev.demobooking.common.domain.CommandHandler;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.UpdateUserPhoto;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.FilesRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@RequiredArgsConstructor
public class UpdateUserPhotoHandler implements CommandHandler<UpdateUserPhoto> {

    private final FilesRepository filesRepository;
    private final DomainEventsBus domainEventsBus;


    @Override
    public void handle(UpdateUserPhoto command, EventId eventId, LocalDateTime issuedAt) {

    }
}
