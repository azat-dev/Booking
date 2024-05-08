package com.azat4dev.demobooking.users.users_commands.data.repositories.jpa;

import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUsersRepository extends JpaRepository<UserData, UUID> {

    Optional<UserData> findByEmail(String email);
}
