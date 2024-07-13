package com.azat4dev.booking.listingsms.common.domain.values;

import com.azat4dev.booking.shared.domain.DomainException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
public final class GuestsCapacity {

    public static final GuestsCapacity DEFAULT = new GuestsCapacity(1, 0, 0);

    private final int adults;
    private final int children;
    private final int infants;

    // Exceptions

    public static GuestsCapacity checkAndMake(int adults, int children, int infants) throws Exception.CapacityMustBePositive {
        if (adults <= 0) {
            throw new Exception.CapacityMustBePositive("adults");
        }
        if (children < 0) {
            throw new Exception.CapacityMustBePositive("children");
        }
        if (infants < 0) {
            throw new Exception.CapacityMustBePositive("infants");
        }
        return new GuestsCapacity(adults, children, infants);
    }

    public static GuestsCapacity dangerouslyMake(int adults, int children, int infants) {
        return new GuestsCapacity(adults, children, infants);
    }

    // Exceptions

    public static abstract class Exception extends DomainException {
        protected Exception(String message) {
            super(message);
        }

        public static final class CapacityMustBePositive extends Exception {
            public CapacityMustBePositive(String field) {
                super("Guests capacity must be positive: " + field);
            }
        }
    }
}
