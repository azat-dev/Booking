package com.azat4dev.booking.users.common.infrastructure.presentation.security.services;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.UserIdNotFoundException;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.entities.UserPrincipal;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public interface CustomUserDetailsService extends UserDetailsService {

    // Types

    @Override
    UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException;

    // Methods

    UserPrincipal loadUserById(UserId userId) throws UserIdNotFoundException;

    UserPrincipal loadUserByEmail(EmailAddress email) throws UserIdNotFoundException;
}
