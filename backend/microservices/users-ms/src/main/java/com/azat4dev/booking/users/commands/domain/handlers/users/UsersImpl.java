package com.azat4dev.booking.users.commands.domain.handlers.users;


import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.commands.domain.core.commands.NewUserData;
import com.azat4dev.booking.users.commands.domain.core.entities.User;
import com.azat4dev.booking.users.commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.commands.domain.core.events.UpdatedUserPhoto;
import com.azat4dev.booking.users.commands.domain.core.events.UserSignedUp;
import com.azat4dev.booking.users.commands.domain.core.events.UserVerifiedEmail;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UsersRepository;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Observed
@Slf4j
@RequiredArgsConstructor
public class UsersImpl implements Users {

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

            log.atInfo().addKeyValue("userId", userId).log("User created");

        } catch (UsersRepository.Exception.UserWithSameEmailAlreadyExists e) {

            log.atInfo().setCause(e).log("User with same email already exists");
            throw new Exception.UserWithSameEmailAlreadyExists();
        } catch (Throwable e) {
            log.atError().setCause(e).log("Failed to create user");
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

            log.atInfo().log("Email verified");

        } catch (java.lang.Exception e) {
            final var logEvent = log.atError().setCause(e);

            switch (e) {
                case UsersRepository.Exception.UserNotFound inst:
                    logEvent.log("User not found");
                    throw new Exception.UserNotFound();
                case User.Exception.VerifiedEmailDoesntExist inst:
                    logEvent.log("Email not found");
                    throw new Exception.EmailNotFound();
                default:
                    logEvent.log("Failed to verify email");
                    throw new RuntimeException(e);
            }
        }

        markOutboxNeedsSynchronization.execute();
    }

    @Override
    public Optional<User> findByEmail(EmailAddress email) {

        final var unitOfWork = unitOfWorkFactory.make();

        try {
            final var result = unitOfWork.doOrFail(() -> {
                final var usersRepository = unitOfWork.getUsersRepository();
                return usersRepository.findByEmail(email);
            });

            log.atDebug().log("User found by email");

            return result;
        } catch (java.lang.Exception e) {
            log.atInfo().setCause(e).log("Failed to find user by email");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePhoto(UserId userId, UserPhotoPath newPhotoPath) throws Exception.FailedToUpdateUser {

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

            log.atDebug().log("User photo updated");
        } catch (Throwable e) {
            log.atError().setCause(e).log("Failed to update user photo");
            throw new Exception.FailedToUpdateUser();
        }

        markOutboxNeedsSynchronization.execute();
    }

    @FunctionalInterface
    public interface MarkOutboxNeedsSynchronization {
        void execute();
    }
}