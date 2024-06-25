package com.azat4dev.booking.searchlistingsms.commands.domain.interfaces;

import com.azat4dev.booking.searchlistingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.searchlistingsms.commands.domain.values.ListingPhoto;

import java.util.List;

public interface ListingsSearchRepository {

    void addNew(ListingId id, String title, String description, List<ListingPhoto> photo);

    void delete(ListingId id);
}
