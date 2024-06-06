package com.azat4dev.booking.listingsms.commands.data.serializer;

import com.azat4dev.booking.listingsms.commands.domain.events.*;
import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.City;
import com.azat4dev.booking.listingsms.common.domain.values.address.Country;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.listingsms.common.domain.values.address.Street;
import com.azat4dev.booking.listingsms.generated.events.dto.*;
import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.files.UploadFileFormData;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@AllArgsConstructor
public final class DomainEventsSerializerImpl implements DomainEventSerializer {

    private final ObjectMapper objectMapper;

    @Override
    public String serialize(DomainEvent<?> event) {

        final var payload = toDTO(event.payload());
        final var dto = ListingsDomainEventDTO.builder()
            .eventId(event.id().getValue())
            .occurredAt(map(event.issuedAt()))
            .payload(payload)
            .build();

        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private UploadFileFormDataDTO toDTO(UploadFileFormData formData) {
        try {
            return UploadFileFormDataDTO.builder()
                .url(formData.url().toURI())
                .bucketName(formData.bucketName().toString())
                .objectName(formData.objectName().toString())
                .fields(formData.formData())
                .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private AddressDTO map(ListingAddress address) {
        return AddressDTO.builder()
            .country(address.getCountry().getValue())
            .city(address.getCity().getValue())
            .street(address.getStreet().getValue())
            .build();
    }

    private ListingAddress map(AddressDTO address) {
        return ListingAddress.dangerouslyMakeFrom(
            Country.dangerouslyMakeFrom(address.getCountry()),
            City.dangerouslyMakeFrom(address.getCity()),
            Street.dangerouslyMakeFrom(address.getStreet())
        );
    }

    private GuestsCapacityDTO map(GuestsCapacity guestsCapacity) {
        return GuestsCapacityDTO.builder()
            .adults(guestsCapacity.getAdults())
            .children(guestsCapacity.getChildren())
            .infants(guestsCapacity.getInfants())
            .build();
    }

    private GuestsCapacity map(GuestsCapacityDTO guestsCapacity) {
        return GuestsCapacity.dangerouslyMake(
            guestsCapacity.getAdults(),
            guestsCapacity.getChildren(),
            guestsCapacity.getInfants()
        );
    }

    private <T> JsonNullable<T> toNullable(OptionalField<T> field) {
        return field.isMissed() ? JsonNullable.undefined() : JsonNullable.of(field.get());
    }

    private <T> JsonNullable<T> toOptionalNullable(OptionalField<Optional<T>> field) {
        return field.isMissed() ? JsonNullable.undefined() : JsonNullable.of(field.get().orElse(null));
    }

    private ListingDetailsFieldsDTO map(ListingDetailsUpdated.Change change) {

        final var title = change.title().map(ListingTitle::getValue);

        final var status = change.status()
            .map(Enum::name)
            .map(ListingStatusDTO::fromValue);

        final var description = change.description().map(v -> v.map(ListingDescription::getValue));

        final var propertyType = change.propertyType()
            .map(v -> v.map(Enum::name)
                .map(PropertyTypeDTO::valueOf));

        final var roomType = change.roomType()
            .map(v -> v.map(Enum::name)
                .map(RoomTypeDTO::valueOf));

        final var guestsCapacity = change.guestsCapacity()
            .map(this::map);

        final var address = change.address()
            .map(v -> v.map(this::map));

        return ListingDetailsFieldsDTO.builder()
            .status(toNullable(status))
            .title(toNullable(title))
            .description(toOptionalNullable(description))
            .propertyType(toOptionalNullable(propertyType))
            .roomType(toOptionalNullable(roomType))
            .guestCapacity(toNullable(guestsCapacity))
            .address(toOptionalNullable(address))
            .build();
    }

    private ListingsDomainEventPayloadDTO toDTO(DomainEventPayload payload) {

        return switch (payload) {
            case NewListingAdded p -> NewListingAddedDTO.builder()
                .listingId(p.listingId().getValue())
                .hostId(p.hostId().getValue())
                .title(p.title().getValue())
                .build();

            case FailedToAddNewListing p -> FailedToAddNewListingDTO.builder()
                .listingId(p.listingId().getValue())
                .hostId(p.hostId().getValue())
                .title(p.title().getValue())
                .build();

            case GeneratedUrlForUploadListingPhoto p -> GeneratedUrlForUploadListingPhotoDTO.builder()
                .userId(p.userId().value())
                .listingId(p.listingId().getValue())
                .formData(toDTO(p.formData()))
                .build();

            case FailedGenerateUrlForUploadListingPhoto p -> FailedGenerateUrlForUploadListingPhotoDTO.builder()
                .operationId(p.operationId().value())
                .userId(p.userId().value())
                .listingId(p.listingId().getValue())
                .fileExtension(p.fileExtension().toString())
                .fileSize(p.fileSize())
                .build();

            case AddedNewPhotoToListing p -> AddedNewPhotoToListingDTO.builder()
                .listingId(p.listingId().getValue())
                .photo(ListingPhotoDTO.builder()
                    .id(p.photo().getId())
                    .bucketName(p.photo().getBucketName().getValue())
                    .objectName(p.photo().getObjectName().getValue())
                    .build()
                ).build();

            case ListingDetailsUpdated p -> ListingDetailsUpdatedDTO.builder()
                .updatedAt(map(p.updatedAt()))
                .listingId(p.listingId().getValue())
                .newValues(map(p.newState()))
                .prevValues(map(p.previousState()))
                .build();

            default ->
                throw new RuntimeException("Serialization. Unknown payload type: " + payload.getClass().getName());
        };
    }

    public long map(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public LocalDateTime map(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    @Override
    public DomainEvent<?> deserialize(String event) {

        try {
            final var dto = objectMapper.readValue(event, ListingsDomainEventDTO.class);
            final var payload = toDomain(dto.getPayload());
            return new DomainEvent<>(
                EventId.dangerouslyCreateFrom(dto.getEventId()),
                map(dto.getOccurredAt()),
                payload
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private UploadFileFormData toDomain(UploadFileFormDataDTO formData) {
        try {
            return new UploadFileFormData(
                formData.getUrl().toURL(),
                BucketName.makeWithoutChecks(formData.getBucketName()),
                MediaObjectName.dangerouslyMake(formData.getObjectName()),
                formData.getFields()
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private ListingDetailsUpdated.Change map(ListingDetailsFieldsDTO f) {
        return new ListingDetailsUpdated.Change(
            OptionalField.from(f.getStatus()).map(Enum::name).map(ListingStatus::valueOf),
            OptionalField.from(f.getTitle()).map(ListingTitle::dangerouslyMakeFrom),
            OptionalField.fromNullable(f.getDescription()).map(v -> v.map(ListingDescription::dangerouslyMakeFrom)),
            OptionalField.fromNullable(f.getPropertyType(), v -> PropertyType.valueOf(v.name())),
            OptionalField.fromNullable(f.getRoomType(), v -> RoomType.valueOf(v.name())),
            OptionalField.from(f.getGuestCapacity()).map(this::map),
            OptionalField.fromNullable(f.getAddress(), this::map)
        );
    }

    private DomainEventPayload toDomain(ListingsDomainEventPayloadDTO payload) {

        return switch (payload) {
            case NewListingAddedDTO p -> new NewListingAdded(
                ListingId.dangerouslyMakeFrom(p.getListingId().toString()),
                HostId.dangerouslyMakeFrom(p.getHostId().toString()),
                ListingTitle.dangerouslyMakeFrom(p.getTitle())
            );

            case FailedToAddNewListingDTO p -> new FailedToAddNewListing(
                ListingId.dangerouslyMakeFrom(p.getListingId().toString()),
                HostId.dangerouslyMakeFrom(p.getHostId().toString()),
                ListingTitle.dangerouslyMakeFrom(p.getTitle())
            );

            case GeneratedUrlForUploadListingPhotoDTO p -> new GeneratedUrlForUploadListingPhoto(
                UserId.dangerouslyMakeFrom(p.getUserId().toString()),
                ListingId.dangerouslyMakeFrom(p.getListingId().toString()),
                toDomain(p.getFormData())
            );

            case FailedGenerateUrlForUploadListingPhotoDTO p -> new FailedGenerateUrlForUploadListingPhoto(
                IdempotentOperationId.dangerouslyMakeFrom(p.getOperationId()),
                UserId.dangerouslyMakeFrom(p.getUserId().toString()),
                ListingId.dangerouslyMakeFrom(p.getListingId().toString()),
                PhotoFileExtension.dangerouslyMakeFrom(p.getFileExtension()),
                p.getFileSize()
            );

            case AddedNewPhotoToListingDTO p -> new AddedNewPhotoToListing(
                ListingId.dangerouslyMakeFrom(p.getListingId().toString()),
                new ListingPhoto(
                    p.getPhoto().getId(),
                    BucketName.makeWithoutChecks(p.getPhoto().getBucketName()),
                    MediaObjectName.dangerouslyMake(p.getPhoto().getObjectName())
                )
            );

            case ListingDetailsUpdatedDTO p -> new ListingDetailsUpdated(
                ListingId.dangerouslyMakeFrom(p.getListingId().toString()),
                map(p.getUpdatedAt()),
                map(p.getPrevValues()),
                map(p.getNewValues())
            );

            default ->
                throw new RuntimeException("Deserialization. Unknown payload type: " + payload.getClass().getName());
        };
    }
}
