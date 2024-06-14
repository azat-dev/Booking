package com.azat4dev.booking.users.users_commands.domain.handlers.users;


import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.users_commands.domain.core.commands.NewUserData;
import com.azat4dev.booking.users.users_commands.domain.core.entities.User;
import com.azat4dev.booking.users.users_commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.users_commands.domain.core.events.UpdatedUserPhoto;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserSignedUp;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserVerifiedEmail;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public final class UsersImpl implements Users {

    private final TimeProvider timeProvider;
    private final MarkOutboxNeedsSynchronization markOutboxNeedsSynchronization;
    private final UnitOfWorkFactory unitOfWorkFactory;

    @Override
    public void createNew(NewUserData newUserData) throws Exception.UserWithSameEmailAlreadyExists, User.Exception {

        final var userId = newUserData.userId();
        final var currentDate = timeProvider.currentTime();

        final var user = User.checkAndMake(
            userId,
            currentDate,
            currentDate,
            newUserData.email(),
            newUserData.fullName(),
            newUserData.encodedPassword(),
            Optional.empty()
        );

        final var unitOfWork = unitOfWorkFactory.make();

        try {
            unitOfWork.doOrFail(() -> {

                final var usersRepository = unitOfWork.getUsersRepository();
                final var outboxEventsRepository = unitOfWork.getOutboxEventsRepository();

                usersRepository.addNew(user);

                outboxEventsRepository.publish(
                    new UserSignedUp(
                        currentDate,
                        userId,
                        newUserData.fullName(),
                        newUserData.email(),
                        EmailVerificationStatus.NOT_VERIFIED
                    )
                );

                return null;
            });

        } catch (UsersRepository.Exception.UserWithSameEmailAlreadyExists e) {
            throw new Exception.UserWithSameEmailAlreadyExists();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        markOutboxNeedsSynchronization.execute();
    }

    @Override
    public void addVerifiedEmail(UserId userId, EmailAddress email) throws Exception.UserNotFound, Exception.EmailNotFound {

        final var unitOfWork = unitOfWorkFactory.make();

        try {

            unitOfWork.doOrFail(() -> {
                final var usersRepository = unitOfWork.getUsersRepository();
                final var outboxEventsRepository = unitOfWork.getOutboxEventsRepository();

                final var user = usersRepository.findById(userId)
                    .orElseThrow(Exception.UserNotFound::new);

                user.verifyEmail(email);
                usersRepository.update(user);

                outboxEventsRepository.publish(new UserVerifiedEmail(userId, email));

                return null;
            });

        } catch (java.lang.Exception e) {
            switch (e) {
                case UsersRepository.Exception.UserNotFound inst:
                    throw new Exception.UserNotFound();
                case User.Exception.VerifiedEmailDoesntExist inst:
                    throw new Exception.EmailNotFound();
                default:
                    throw new RuntimeException(e);
            }
        }

        markOutboxNeedsSynchronization.execute();
    }

    @Override
    public Optional<User> findByEmail(EmailAddress email) {

        final var unitOfWork = unitOfWorkFactory.make();

        try {
            return unitOfWork.doOrFail(() -> {
                final var usersRepository = unitOfWork.getUsersRepository();
                return usersRepository.findByEmail(email);
            });
        } catch (java.lang.Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePhoto(UserId userId, UserPhotoPath newPhotoPath) throws Exception.UserNotFound, Exception.FailedToUpdateUser {

        final var unitOfWork = unitOfWorkFactory.make();

        try {
            unitOfWork.doOrFail(() -> {
                final var usersRepository = unitOfWork.getUsersRepository();

                final var user = usersRepository.findById(userId)
                    .orElseThrow(Exception.UserNotFound::new);

                final var prevPhoto = user.getPhoto();

                user.setPhoto(newPhotoPath);
                usersRepository.update(user);

                final var outboxRepository = unitOfWork.getOutboxEventsRepository();

                final var event = new UpdatedUserPhoto(
                    userId,
                    newPhotoPath,
                    prevPhoto
                );

                outboxRepository.publish(event);
                return null;
            });

        } catch (Throwable e) {
            throw new Exception.FailedToUpdateUser();
        }

        markOutboxNeedsSynchronization.execute();
    }

    @FunctionalInterface
    public interface MarkOutboxNeedsSynchronization {
        void execute();
    }
}