package com.azat4dev.booking.users.common.infrastructure.presentation.security.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public final class UserDetailsServiceFacade implements UserDetailsService {

    private final UserDetailsService[] services;

    public UserDetailsServiceFacade(UserDetailsService... services) {
        this.services = services;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        for (UserDetailsService service : services) {
            try {
                return service.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                log.error("User not found: {}", username);
            }
        }

        throw new UsernameNotFoundException("User not found");
    }
}
