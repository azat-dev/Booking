package com.azat4dev.demobooking.users.presentation.security.services;

import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;
import com.azat4dev.demobooking.users.presentation.security.entities.UserPrincipal;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.UserIdNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UsersRepository usersRepository;

    public CustomUserDetailsServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            final var userId = UserId.fromString(username);
            return loadUserById(userId);
        } catch (UserId.WrongFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserPrincipal loadUserById(UserId userId) throws UserIdNotFoundException {

        final var user = usersRepository.findById(userId);

        return user.map(UserPrincipal::from)
            .orElseThrow(() -> new UserIdNotFoundException("User not found"));
    }

    @Override
    public UserPrincipal loadUserByEmail(EmailAddress email) throws UserIdNotFoundException {

        final var user = usersRepository.findByEmail(email);

        return user.map(UserPrincipal::from)
            .orElseThrow(() -> new UserIdNotFoundException("User not found"));
    }
}