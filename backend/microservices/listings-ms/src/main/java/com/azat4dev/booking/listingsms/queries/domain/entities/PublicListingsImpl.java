package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PublicListingsReadRepository;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;

import java.util.Optional;

@Observed
@AllArgsConstructor
public class PublicListingsImpl implements PublicListings {

    private final PublicListingsReadRepository repository;

    @Override
    public Optional<ListingPublicDetails> findById(ListingId listingId) throws Exception.ListingNotPublished {
        try {
            return repository.findById(listingId);
        } catch (PublicListingsReadRepository.Exception.ListingNotPublished e) {
            throw new Exception.ListingNotPublished(listingId);
        }
    }
}
