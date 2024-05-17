package com.azat4dev.demobooking.users.users_commands.domain.handlers.users;

import com.azat4dev.demobooking.common.domain.CommandHandler;
import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.UpdateUserPhoto;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.FailedUpdateUserPhoto;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UpdatedUserPhoto;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectsBucket;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UnitOfWorkFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@RequiredArgsConstructor
public final class UpdateUserPhotoHandler implements CommandHandler<UpdateUserPhoto> {

    private final MediaObjectsBucket mediaObjectsBucket;
    private final UnitOfWorkFactory unitOfWorkFactory;
    private final DomainEventsBus bus;

    @Override
    public void handle(UpdateUserPhoto command, EventId eventId, LocalDateTime issuedAt) throws UpdateUserPhotoHandler.Exception {

        final var mediaObject = mediaObjectsBucket.getObject(command.uploadedFileData().objectName());
        final var unitOfWork = unitOfWorkFactory.make();

        try {
            final var usersRepository = unitOfWork.getUsersRepository();

            final var user = usersRepository.findById(command.userId())
                .orElseThrow(() -> new UpdateUserPhotoHandler.Exception.UserNotFound(command.userId()));

            final var prePhotoPath = user.getPhoto();

            final var newPhotoPath = new UserPhotoPath(
                mediaObject.bucketName(),
                mediaObject.objectName()
            );

            user.setPhoto(newPhotoPath);
            usersRepository.update(user);

            final var outboxRepository = unitOfWork.getOutboxEventsRepository();

            final var event = new UpdatedUserPhoto(
                command.idempotentOperationId(),
                command.userId(),
                newPhotoPath,
                prePhotoPath
            );

            outboxRepository.publish(event);
            unitOfWork.save();

        } catch (Throwable e) {
            unitOfWork.rollback();
            bus.publish(new FailedUpdateUserPhoto(command.idempotentOperationId(), command.userId(), command.uploadedFileData()));
        }
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
