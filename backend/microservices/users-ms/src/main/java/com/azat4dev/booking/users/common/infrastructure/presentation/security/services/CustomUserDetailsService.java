package com.azat4dev.booking.users.common.infrastructure.presentation.security.services;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.UserIdNotFoundException;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.entities.UserPrincipal;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UsersRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public final class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            final var userId = UserId.checkAndMakeFrom(username);
            return loadUserById(userId);
        } catch (UserId.WrongFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private UserPrincipal loadUserById(UserId userId) throws UserIdNotFoundException {

        final var user = usersRepository.findById(userId);

        return user.map(UserPrincipal::from)
            .orElseThrow(() -> new UserIdNotFoundException("User not found"));
    }
}