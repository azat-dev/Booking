package com.azat4dev.booking.listingsms.e2e.helpers;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.entities.Listings;
import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.City;
import com.azat4dev.booking.listingsms.common.domain.values.address.Country;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.listingsms.common.domain.values.address.Street;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.test.context.TestComponent;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestComponent
@AllArgsConstructor
public class ListingHelpers {

    private final PhotoHelpers photoHelpers;
    private final Faker faker = new Faker();
    private final MakeNewListingId makeNewListingId;
    private final Listings listings;

    public ListingTitle anyListingTitle() throws Exception {
        return ListingTitle.checkAndMakeFrom(faker.book().title());
    }

    public ListingAddress anyAddress() {
        return new ListingAddress(
            Country.dangerouslyMakeFrom(faker.address().country()),
            City.dangerouslyMakeFrom(faker.address().city()),
            Street.dangerouslyMakeFrom(faker.address().streetName())
        );
    }

    public Listing givenExistingListing(UserId userId) throws Exception {

        final var listingId = makeNewListingId.make();
        listings.addNew(
            listingId,
            HostId.fromUserId(userId),
            anyListingTitle()
        );

        return listings.getById(listingId)
            .orElseThrow(() -> new RuntimeException("Listing not found"));
    }

    public Listing givenListingReadyForPublishing(UserId userId) throws Exception {

        final var listing = givenExistingListing(userId);

        listing.setDescription(
            Optional.of(
                ListingDescription.checkAndMakeFrom(
                    faker.lorem().paragraph()
                )
            )
        );
        listing.setPropertyType(Optional.of(PropertyType.APARTMENT));
        listing.setRoomType(Optional.of(RoomType.PRIVATE_ROOM));
        listing.setAddress(Optional.of(anyAddress()));

        listings.update(listing);

        photoHelpers.givenAllPhotosUploaded(userId, listing.getId());

        final var updatedListing = listings.getById(listing.getId())
            .orElseThrow(() -> new RuntimeException("Listing not found"));

        assertThat(updatedListing.getStatus()).isEqualTo(ListingStatus.READY_FOR_PUBLISHING);

        return updatedListing;
    }

    public Listing givenPublishedListing(UserId userId) throws Exception {

        final var listing = givenListingReadyForPublishing(userId);

        listing.publish();
        listings.update(listing);

        assertThat(listing.getStatus()).isEqualTo(ListingStatus.PUBLISHED);
        return listing;
    }
}
