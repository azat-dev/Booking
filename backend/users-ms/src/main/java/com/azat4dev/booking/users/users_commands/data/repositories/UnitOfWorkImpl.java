package com.azat4dev.booking.users.users_commands.data.repositories;

import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.UndeclaredThrowableException;


@RequiredArgsConstructor
public class UnitOfWorkImpl extends DefaultTransactionDefinition implements UnitOfWork {

    private final TransactionTemplate transactionTemplate;
    private final OutboxEventsRepository outboxEventsRepository;
    private final UsersRepository usersRepository;
    private Status status = Status.INITIAL;


    @Override
    public OutboxEventsRepository getOutboxEventsRepository() {
        return this.outboxEventsRepository;
    }

    @Override
    public UsersRepository getUsersRepository() {
        return this.usersRepository;
    }

    @Override
    public <T> T doOrFail(Action<T> action) throws Exception {
        if (status != Status.INITIAL) {
            throw new IllegalStateException("UnitOfWork already committed or rolled back");
        }

        try {
            final var result = transactionTemplate.execute(status -> {
                try {
                    return action.run();
                } catch (Exception e) {
                    throw new UndeclaredThrowableException(e);
                }
            });

            this.status = Status.COMMITTED;
            return result;

        } catch (UndeclaredThrowableException e) {
            this.status = Status.ROLLED_BACK;
            if (e.getUndeclaredThrowable() instanceof Exception) {
                throw (Exception) e.getUndeclaredThrowable();
            }
            throw e;
        } catch (Throwable e) {
            this.status = Status.ROLLED_BACK;
            throw e;
        }

    }

    enum Status {
        INITIAL,
        COMMITTED,
        ROLLED_BACK
    }
}
