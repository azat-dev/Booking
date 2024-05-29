package com.azat4dev.booking.users.users_commands.domain.handlers.users;


import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.users_commands.domain.core.commands.NewUserData;
import com.azat4dev.booking.users.users_commands.domain.core.entities.User;
import com.azat4dev.booking.users.users_commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.users_commands.domain.core.events.UpdatedUserPhoto;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserCreated;
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
    public void createNew(NewUserData newUserData) throws Exception.UserWithSameEmailAlreadyExists {

        final var userId = newUserData.userId();
        final var currentDate = timeProvider.currentTime();

        final var unitOfWork = unitOfWorkFactory.make();

        try {

            final var usersRepository = unitOfWork.getUsersRepository();
            final var outboxEventsRepository = unitOfWork.getOutboxEventsRepository();

            final var user = User.checkAndMake(
                userId,
                currentDate,
                currentDate,
                newUserData.email(),
                newUserData.fullName(),
                newUserData.encodedPassword(),
                Optional.empty()
            );

            usersRepository.addNew(user);

            outboxEventsRepository.publish(
                new UserCreated(
                    currentDate,
                    userId,
                    newUserData.fullName(),
                    newUserData.email(),
                    EmailVerificationStatus.NOT_VERIFIED
                )
            );

            unitOfWork.save();

        } catch (UsersRepository.Exception.UserWithSameEmailAlreadyExists e) {
            unitOfWork.rollback();
            throw new Exception.UserWithSameEmailAlreadyExists();
        } catch (Throwable e) {
            unitOfWork.rollback();
            throw new RuntimeException(e);
        }

        markOutboxNeedsSynchronization.execute();
    }

    @Override
    public void addVerifiedEmail(UserId userId, EmailAddress email) throws Exception.UserNotFound, Exception.EmailNotFound {

        final var unitOfWork = unitOfWorkFactory.make();

        try {

            final var usersRepository = unitOfWork.getUsersRepository();
            final var outboxEventsRepository = unitOfWork.getOutboxEventsRepository();

            final var user = usersRepository.findById(userId)
                .orElseThrow(Exception.UserNotFound::new);

            user.verifyEmail(email);
            usersRepository.update(user);

            outboxEventsRepository.publish(new UserVerifiedEmail(userId, email));

            unitOfWork.save();

        } catch (UsersRepository.Exception.UserNotFound e) {
            unitOfWork.rollback();
            throw new Exception.UserNotFound();
        } catch (User.Exception.VerifiedEmailDoesntExist e) {
            unitOfWork.rollback();
            throw new Exception.EmailNotFound();
        } catch (Throwable e) {
            unitOfWork.rollback();
            throw new RuntimeException(e);
        }

        markOutboxNeedsSynchronization.execute();
    }

    @Override
    public Optional<User> findByEmail(EmailAddress email) {

        final var unitOfWork = unitOfWorkFactory.make();

        try {
            final var usersRepository = unitOfWork.getUsersRepository();
            final var foundUser = usersRepository.findByEmail(email);
            unitOfWork.save();

            return foundUser;
        } catch (Throwable e) {
            unitOfWork.rollback();
        }

        return Optional.empty();
    }

    @Override
    public void updatePhoto(UserId userId, UserPhotoPath newPhotoPath) throws Exception.UserNotFound, Exception.FailedToUpdateUser {

        final var unitOfWork = unitOfWorkFactory.make();

        try {
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
            unitOfWork.save();

        } catch (Throwable e) {
            unitOfWork.rollback();
            throw new Exception.FailedToUpdateUser();
        }

        markOutboxNeedsSynchronization.execute();
    }

    @FunctionalInterface
    public interface MarkOutboxNeedsSynchronization {
        void execute();
    }
}