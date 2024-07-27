package com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPublicDetails;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PublicListingsReadRepository;
import com.azat4dev.booking.listingsms.queries.infrastructure.persistence.dao.ListingsReadDao;
import com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers.MapRecordToListingPublicDetails;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;

import java.util.Optional;

@Observed
@AllArgsConstructor
public class PublicListingsReadRepositoryImpl implements PublicListingsReadRepository {

    private final ListingsReadDao dao;
    private final MapRecordToListingPublicDetails mapper;

    @Override
    public Optional<ListingPublicDetails> findById(ListingId id) throws Exception.ListingNotPublished {

        try {
            return dao.findById(id.getValue())
                .map(mapper::map);

        } catch (MapRecordToListingPublicDetails.Exception.ListingNotPublished e) {
            throw new Exception.ListingNotPublished(id);
        }
    }
}
