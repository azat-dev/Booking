package com.azat4dev.booking.users.users_commands.data.repositories;

import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Optional;


public class UnitOfWorkImpl extends DefaultTransactionDefinition implements UnitOfWork {

    private final OutboxEventsRepository outboxEventsRepository;
    private final UsersRepository usersRepository;
    private final PlatformTransactionManager transactionManager;
    private Status status = Status.INITIAL;
    private Optional<TransactionStatus> transactionStatus;


    public UnitOfWorkImpl(
        PlatformTransactionManager transactionManager,
        OutboxEventsRepository outboxEventsRepository,
        UsersRepository usersRepository
    ) {

        this.setIsolationLevel(ISOLATION_READ_COMMITTED);
        this.transactionManager = transactionManager;
        this.outboxEventsRepository = outboxEventsRepository;
        this.usersRepository = usersRepository;
        this.transactionStatus = Optional.of(this.transactionManager.getTransaction(this));
    }

    @Override
    public void save() {

        if (this.status != Status.INITIAL || this.transactionStatus.isEmpty()) {
            throw new RuntimeException("Cannot save a transaction that is not in the initial state");
        }

        final var s = this.transactionStatus.get();

        this.transactionManager.commit(s);
        this.status = Status.COMMITTED;
        this.transactionStatus = Optional.empty();
    }

    @Override
    public void rollback() {

        if (this.status != Status.INITIAL || this.transactionStatus.isEmpty()) {
            throw new RuntimeException("Cannot rollback a transaction that is not in the initial state");
        }

        transactionManager.rollback(this.transactionStatus.get());

        this.status = Status.ROLLED_BACK;
        this.transactionStatus = Optional.empty();
    }

    @Override
    public OutboxEventsRepository getOutboxEventsRepository() {
        return this.outboxEventsRepository;
    }

    @Override
    public UsersRepository getUsersRepository() {
        return this.usersRepository;
    }

    enum Status {
        INITIAL,
        COMMITTED,
        ROLLED_BACK
    }
}
