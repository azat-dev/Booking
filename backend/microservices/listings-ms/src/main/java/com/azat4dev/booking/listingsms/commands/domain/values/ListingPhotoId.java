package com.azat4dev.booking.listingsms.commands.domain.values;

import com.azat4dev.booking.shared.domain.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@EqualsAndHashCode(of = "value")
@Getter
public final class ListingPhotoId {

    private final UUID value;

    private ListingPhotoId(UUID value) {
        this.value = value;
    }

    public static ListingPhotoId checkAndMake(String value) throws Exception.WrongFormat {
        try {
            return new ListingPhotoId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new Exception.WrongFormat();
        }
    }

    public static ListingPhotoId makeWithoutChecks(String value) {
        return new ListingPhotoId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public static ListingPhotoId generateNew() {
        return new ListingPhotoId(UUID.randomUUID());
    }

    // Exceptions

    public static class Exception extends DomainException {
        protected Exception(String message) {
            super(message);
        }

        public static class WrongFormat extends Exception {
            public WrongFormat() {
                super("Listing photo ID has wrong format");
            }
        }
    }
}
