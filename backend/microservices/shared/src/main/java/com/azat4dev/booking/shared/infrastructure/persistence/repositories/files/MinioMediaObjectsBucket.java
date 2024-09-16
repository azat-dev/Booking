package com.azat4dev.booking.shared.infrastructure.persistence.repositories.files;

import com.azat4dev.booking.shared.domain.interfaces.files.MediaObject;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import com.azat4dev.booking.shared.domain.values.BaseUrl;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.UploadFileFormData;
import io.minio.*;
import io.minio.http.Method;
import lombok.AllArgsConstructor;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.ZonedDateTime;

@AllArgsConstructor
public class MinioMediaObjectsBucket implements MediaObjectsBucket {

    private final BaseUrl baseUrl;
    private final MinioClient minioClient;
    private final BucketName bucketName;

    public URL getBucketUrl() throws MalformedURLException {
        return baseUrl.urlWithPath(bucketName.toString());
    }


    public URL getObjectUrl(MediaObjectName objectName) throws MalformedURLException {
        return baseUrl.urlWithPath(bucketName.toString() + "/" + objectName.toString());
    }

    @Override
    public URL generateUploadUrl(MediaObjectName objectName, Duration expiresIn) {

        try {
            final var url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName.toString())
                    .object(objectName.toString())
                    .method(Method.PUT)
                    .expiry((int) expiresIn.toSeconds())
                    .build()
            );
            return URI.create(url).toURL();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UploadFileFormData generateUploadFormData(MediaObjectName objectName, Duration expiresIn, Policy policy) {

        final var formPolicy = new PostPolicy(
            bucketName.toString(),
            ZonedDateTime.now().plusSeconds((int) expiresIn.toSeconds())
        );

        for (var condition : policy.conditions()) {
            switch (condition.type()) {
                case EQUALS -> formPolicy.addEqualsCondition(condition.key(), condition.value());
                case STARTS_WITH -> formPolicy.addStartsWithCondition(condition.key(), condition.value());
            }
        }

        formPolicy.addEqualsCondition("key", objectName.toString());

        policy.fileSizeRange().ifPresent(fileSizeRange -> {
            formPolicy.addContentLengthRangeCondition(fileSizeRange.min(), fileSizeRange.max());
        });

        final URL url;
        try {
            url = getBucketUrl();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try {

            final var formData = minioClient.getPresignedPostFormData(
                formPolicy
            );

            formData.put("key", objectName.toString());

            return new UploadFileFormData(
                url,
                bucketName,
                objectName,
                formData
            );

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MediaObject getObject(MediaObjectName objectName) {

        final StatObjectResponse response;
        try {
            response = minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName.toString())
                    .object(objectName.toString())
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        final URL objectUrl;
        try {
            objectUrl = getObjectUrl(objectName);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return new MediaObjectImpl(
            bucketName,
            objectName,
            objectUrl,
            response.size(),
            response.lastModified(),
            response.contentType()
        );
    }

    public record MediaObjectImpl(
        BucketName bucketName,
        MediaObjectName objectName,
        URL url,
        long size,
        ZonedDateTime lastModified,
        String contentType
    ) implements MediaObject {
    }
}
