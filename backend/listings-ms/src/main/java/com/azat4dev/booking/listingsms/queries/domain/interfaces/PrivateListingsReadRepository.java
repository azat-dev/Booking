package com.azat4dev.booking.listingsms.queries.domain.interfaces;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;

import java.util.List;
import java.util.Optional;

public interface PrivateListingsReadRepository {

    Optional<ListingPrivateDetails> findById(ListingId id);

    List<ListingPrivateDetails> findAllByOwnerId(OwnerId ownerId);
}
