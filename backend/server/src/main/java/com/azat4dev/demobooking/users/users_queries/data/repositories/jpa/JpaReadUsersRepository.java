package com.azat4dev.demobooking.users.users_queries.data.repositories.jpa;

import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaReadUsersRepository extends JpaRepository<UserData, UUID> {

}
