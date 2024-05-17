package com.azat4dev.demobooking.users.users_commands.domain.handlers.users;

import com.azat4dev.demobooking.common.domain.CommandHandler;
import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.UpdateUserPhoto;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectsBucket;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@RequiredArgsConstructor
public final class UpdateUserPhotoHandler implements CommandHandler<UpdateUserPhoto> {

    private final MediaObjectsBucket mediaObjectsBucket;
    private final UsersRepository usersRepository;
    private final DomainEventsBus domainEventsBus;

    @Override
    public void handle(UpdateUserPhoto command, EventId eventId, LocalDateTime issuedAt) throws UpdateUserPhotoHandler.Exception {

        final var mediaObject = mediaObjectsBucket.getObject(command.uploadedFileData().objectName());

        final var user = usersRepository.findById(command.userId())
            .orElseThrow(() -> new UpdateUserPhotoHandler.Exception.UserNotFound(command.userId()));

        user.setPhoto(new UserPhotoPath(
            mediaObject.bucketName(),
            mediaObject.objectName()
        ));

        usersRepository.update(user);
    }

    public static abstract class Exception extends DomainException {
        Exception(String message) {
            super(message);
        }

        public static final class UserNotFound extends Exception {
            public UserNotFound(UserId userId) {
                super("User not found: " + userId);
            }
        }
    }
}
