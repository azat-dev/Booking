package com.azat4dev.booking.searchlistingsms.common.domain.values.address;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@AllArgsConstructor
@EqualsAndHashCode
@Getter
public final class ListingAddress {

    private final Country country;
    private final City city;
    private final Street street;

    public static ListingAddress dangerouslyMakeFrom(Country country, City city, Street street) {
        return new ListingAddress(country, city, street);
    }

    public static ListingAddress checkAndMakeFrom(Country country, City city, Street street) {
        return new ListingAddress(country, city, street);
    }
}
