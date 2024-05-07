package com.azat4dev.demobooking.users.data.repositories.jpa;

import com.azat4dev.demobooking.users.data.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaUsersRepository extends JpaRepository<UserData, UUID> {

}
