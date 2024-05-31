package com.azat4dev.booking.listingsms.commands.data.repositories.mappers;

import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingPhotoData;
import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.jooq.JSON;
import org.jooq.generated.tables.records.ListingsRecord;

@AllArgsConstructor
public class MapListingToRecordImpl implements MapListingToRecord {

    private final ObjectMapper objectMapper;

    @Override
    public ListingsRecord map(Listing listing) {

        final var address = listing.getAddress();
        final var o = new ListingsRecord();

        o.setId(listing.getId().getValue());

        o.setCreatedAt(listing.getCreatedAt().withNano(0));
        o.setCreatedAtNano(listing.getCreatedAt().getNano());

        o.setUpdatedAt(listing.getUpdatedAt().withNano(0));
        o.setUpdatedAtNano(listing.getUpdatedAt().getNano());

        o.setTitle(listing.getTitle().getValue());
        o.setDescription(listing.getDescription().map(ListingDescription::getValue).orElse(null));

        o.setOwnerId(listing.getOwnerId().getValue());

        o.setStatus(listing.getStatus().name());

        o.setPropertyType(listing.getPropertyType().map(Enum::name).orElse(null));
        o.setRoomType(listing.getRoomType().map(Enum::name).orElse(null));

        o.setGuestsCapacityAdults(listing.getGuestsCapacity().getAdults());
        o.setGuestsCapacityChildren(listing.getGuestsCapacity().getChildren());
        o.setGuestsCapacityInfants(listing.getGuestsCapacity().getInfants());

        o.setAddressCountry(address.map(a -> a.getCountry().getValue()).orElse(null));
        o.setAddressCity(address.map(a -> a.getCity().getValue()).orElse(null));
        o.setAddressStreet(address.map(a -> a.getStreet().getValue()).orElse(null));

        final var photos = listing.getPhotos().stream().map(ListingPhotoData::fromDomain).toList();

        try {
            o.setPhotos(photos.isEmpty() ? null : JSON.valueOf(objectMapper.writeValueAsString(photos)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return o;
    }
}
