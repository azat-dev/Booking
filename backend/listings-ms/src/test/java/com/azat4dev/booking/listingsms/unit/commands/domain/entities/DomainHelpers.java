package com.azat4dev.booking.listingsms.unit.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.github.javafaker.Faker;

import java.util.UUID;

public class DomainHelpers {

    private final static Faker faker = new Faker();

    public static HostId anyHostId() {
        return HostId.dangerouslyMakeFrom(UUID.randomUUID().toString());
    }

    public static ListingId anyListingId() {
        return ListingId.dangerouslyMakeFrom(UUID.randomUUID().toString());
    }

    public static ListingTitle anyListingTitle() {
        try {
            return ListingTitle.checkAndMakeFrom(faker.name().title());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
