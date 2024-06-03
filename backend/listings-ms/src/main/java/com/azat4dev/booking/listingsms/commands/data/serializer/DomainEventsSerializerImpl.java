package com.azat4dev.booking.listingsms.commands.data.serializer;

import com.azat4dev.booking.listingsms.commands.domain.events.FailedGenerateUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.events.FailedToAddNewListing;
import com.azat4dev.booking.listingsms.commands.domain.events.GeneratedUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.events.NewListingAdded;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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

            default ->
                throw new RuntimeException("Deserialization. Unknown payload type: " + payload.getClass().getName());
        };
    }
}
