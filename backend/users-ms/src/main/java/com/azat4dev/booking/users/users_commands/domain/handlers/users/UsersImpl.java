package com.azat4dev.booking.users.users_commands.domain.handlers.users;


import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.shared.domain.event.EventId;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.users_commands.domain.core.commands.NewUserData;
import com.azat4dev.booking.users.users_commands.domain.core.commands.UpdateUserPhoto;
import com.azat4dev.booking.users.users_commands.domain.core.entities.User;
import com.azat4dev.booking.users.users_commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.users_commands.domain.core.events.FailedUpdateUserPhoto;
import com.azat4dev.booking.users.users_commands.domain.core.events.UpdatedUserPhoto;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserVerifiedEmail;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
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
            markOutboxNeedsSynchronization.execute();

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
    }

    @FunctionalInterface
    public interface MarkOutboxNeedsSynchronization {
        void execute();
    }
}