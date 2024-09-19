package com.azat4dev.booking.searchlistingsms.config.common.infrastructure.bus;

public enum InternalTopics {
    LISTING_PUBLISHED("internal-listing-published");

    private final String value;

    InternalTopics(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
