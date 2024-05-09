package com.azat4dev.demobooking.users.users_queries.presentation.api.rest.resources;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
