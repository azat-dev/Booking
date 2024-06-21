package com.azat4dev.booking.users.common.infrastructure.presentation.security.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
            }
        }

        throw new UsernameNotFoundException("User not found");
    }
}
