package com.azat4dev.demobooking.users.users_commands.data.repositories.jpa;

import com.azat4dev.demobooking.users.users_commands.data.entities.OutboxEventData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOutboxEventsRepository extends JpaRepository<OutboxEventData, String> {

    List<OutboxEventData> findAllByPublishedFalseOrderByOrder(Pageable pageable);
}
