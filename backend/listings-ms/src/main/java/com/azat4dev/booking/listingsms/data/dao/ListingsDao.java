package com.azat4dev.booking.listingsms.data.dao;

import com.azat4dev.booking.listingsms.data.dao.entities.ListingData;

import java.util.Optional;
import java.util.UUID;

public interface ListingsDao {

    void addNew(ListingData listing) throws Exception.ListingAlreadyExists;

    Optional<ListingData> findById(UUID id);

    // Exceptions

    class Exception extends RuntimeException {
        static class ListingAlreadyExists extends Exception {
        }
    }
}
