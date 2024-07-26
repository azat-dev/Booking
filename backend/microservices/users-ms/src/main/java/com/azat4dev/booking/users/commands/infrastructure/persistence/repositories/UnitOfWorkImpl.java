package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories;

import com.azat4dev.booking.shared.infrastructure.persistence.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UsersRepository;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.UndeclaredThrowableException;

@Observed
@Slf4j
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
            log.atError().log("UnitOfWork already committed or rolled back");
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
            log.atError().setCause(e).log("UnitOfWork failed");
            this.status = Status.ROLLED_BACK;
            if (e.getUndeclaredThrowable() instanceof Exception) {
                throw (Exception) e.getUndeclaredThrowable();
            }
            throw e;
        } catch (Throwable e) {
            log.atError().setCause(e).log("UnitOfWork failed");
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
