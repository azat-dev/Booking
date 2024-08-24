package com.azat4dev.booking.listingsms.config.common.infrastructure.bus;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingStatus;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.listingsms.common.infrastructure.serializer.mappers.*;
import com.azat4dev.booking.listingsms.generated.api.bus.dto.listingsms.AddressDTO;
import com.azat4dev.booking.listingsms.generated.api.bus.dto.listingsms.GuestsCapacityDTO;
import com.azat4dev.booking.listingsms.generated.api.bus.dto.listingsms.ListingPublicDetailsDTO;
import com.azat4dev.booking.listingsms.generated.api.bus.dto.listingsms.ListingStatusDTO;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPublicDetails;
import com.azat4dev.booking.listingsms.queries.infrastructure.serializer.mappers.MapListingPublicDetails;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class MapValuesConfig {

    @Bean
    public Serializer<ListingAddress, AddressDTO> mapAddress() {
        return new MapListingAddress();
    }

    @Bean
    public Serializer<GuestsCapacity, GuestsCapacityDTO> mapGuestsCapacity() {
        return new MapGuestsCapacity();
    }

    @Bean
    public Serializer<ListingStatus, ListingStatusDTO> mapListingStatus() {
        return new MapListingStatus();
    }

    @Bean
    public Serializer<ListingPublicDetails, ListingPublicDetailsDTO> mapListingPublicDetails(
        Serializer<LocalDateTime, String> mapDateTime,
        Serializer<GuestsCapacity, GuestsCapacityDTO> mapGuestCapacity,
        Serializer<ListingStatus, ListingStatusDTO> mapListingStatus,
        Serializer<ListingAddress, AddressDTO> mapAddress
    ) {
        return new MapListingPublicDetails(
            mapListingStatus,
            mapAddress,
            mapGuestCapacity,
            new MapPropertyType(),
            new MapRoomType(),
            new MapListingPhoto(),
            mapDateTime
        );
    }
}
