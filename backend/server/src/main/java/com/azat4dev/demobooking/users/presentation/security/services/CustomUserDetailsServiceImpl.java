package com.azat4dev.demobooking.users.presentation.security.services;

import java.util.UUID;

import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.values.UserId;
import com.azat4dev.demobooking.users.presentation.security.entities.UserPrincipal;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.UserIdNotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UsersRepository usersRepository;

    public CustomUserDetailsServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {

        final var userId = UserId.fromString(username);
        return  loadUserById(userId);
    }

    @Override
    public UserPrincipal loadUserById(UserId userId) throws UserIdNotFoundException {

        final var user = usersRepository.findById(userId);

        return user.map(UserPrincipal::from)
            .orElseThrow(() -> new UserIdNotFoundException("User not found"));
    }
}