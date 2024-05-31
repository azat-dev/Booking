package com.azat4dev.booking.listingsms.queries.data.repositories;

import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.queries.data.dao.ListingRecord;
import com.azat4dev.booking.listingsms.queries.data.dao.ListingsReadDao;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import com.azat4dev.booking.listingsms.queries.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.queries.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.queries.domain.values.RoomType;
import com.azat4dev.booking.listingsms.queries.domain.values.address.City;
import com.azat4dev.booking.listingsms.queries.domain.values.address.Country;
import com.azat4dev.booking.listingsms.queries.domain.values.address.ListingAddress;
import com.azat4dev.booking.listingsms.queries.domain.values.address.Street;
import com.azat4dev.booking.shared.domain.values.BucketName;
import com.azat4dev.booking.shared.domain.values.MediaObjectName;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
public final class PrivateListingsReadRepositoryImpl implements PrivateListingsReadRepository {

    private final ListingsReadDao dao;
    private final MapRecordToPrivateListingDetails mapper = new MapRecordToPrivateListingDetails();

    @Override
    public Optional<ListingPrivateDetails> findById(ListingId id) {
        return dao.findById(id.getValue())
            .map(mapper);
    }

    @Override
    public List<ListingPrivateDetails> findAllByOwnerId(OwnerId ownerId) {
        return dao.findAllByOwnerId(ownerId.getValue())
            .stream()
            .map(mapper)
            .toList();
    }

    public static class MapRecordToPrivateListingDetails implements Function<ListingRecord, ListingPrivateDetails> {

        private ListingAddress mapAddress(ListingRecord.Address address) {
            return ListingAddress.dangerouslyMakeFrom(
                Country.dangerouslyMakeFrom(address.country()),
                City.dangerouslyMakeFrom(address.city()),
                Street.dangerouslyMakeFrom(address.street())
            );
        }

        private ListingPhoto mapListingPhoto(ListingRecord.Photo photo) {
            return new ListingPhoto(
                photo.id(),
                BucketName.makeWithoutChecks(photo.bucketName()),
                MediaObjectName.dangerouslyMake(photo.objectName())
            );
        }

        private List<ListingPhoto> mapPhotos(List<ListingRecord.Photo> photos) {
            return photos.stream()
                .map(this::mapListingPhoto)
                .toList();
        }

        @Override
        public ListingPrivateDetails apply(ListingRecord record) {
            return new ListingPrivateDetails(
                ListingId.dangerouslyMakeFrom(record.id().toString()),
                record.createdAt(),
                record.updatedAt(),
                OwnerId.checkAndMakeFrom(record.ownerId().toString()),
                ListingTitle.dangerouslyMakeFrom(record.title()),
                ListingStatus.valueOf(record.status()),
                record.description().map(ListingDescription::dangerouslyMakeFrom),
                GuestsCapacity.dangerouslyMake(record.guestsCapacity().adults(), record.guestsCapacity().children(), record.guestsCapacity().infants()),
                record.propertyType().map(PropertyType::valueOf),
                record.roomType().map(RoomType::valueOf),
                record.address().map(this::mapAddress),
                mapPhotos(record.photos())
            );
        }
    }
}
