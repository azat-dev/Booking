package com.azat4dev.booking.searchlistingsms.commands.infrastructure.persistence.repositories;

import com.azat4dev.booking.searchlistingsms.commands.domain.interfaces.ListingsSearchRepository;
import com.azat4dev.booking.searchlistingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.searchlistingsms.commands.domain.values.ListingPhoto;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ListingsSearchRepositoryImpl implements ListingsSearchRepository {

    @Override
    public void addNew(ListingId id, String title, String description, List<ListingPhoto> photo) {

    }

    @Override
    public void delete(ListingId id) {

    }
}
