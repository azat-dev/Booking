package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.UploadFileFormData;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.*;
import com.azat4dev.demobooking.users.users_queries.domain.values.BaseUrl;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
public class MinioMediaObjectsBucket implements MediaObjectsBucket {

    private final BaseUrl baseUrl;
    private final MinioClient minioClient;
    private final BucketName bucketName;

    public URL getObjectUrl(MediaObjectName objectName) throws MalformedURLException {
        return baseUrl.urlWithPath(bucketName.toString() + "/" + objectName.toString());
    }

    @Override
    public URL generateUploadUrl(MediaObjectName objectName, int expiresInSeconds) {

        try {
            final var url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName.toString())
                    .object(objectName.toString())
                    .method(Method.PUT)
                    .expiry(expiresInSeconds)
                    .build()
            );

            return new URL(url);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UploadFileFormData generateUploadFormData(MediaObjectName objectName, int expiresInSeconds, Policy policy) {

        final var formPolicy = new PostPolicy(
            bucketName.toString(),
            ZonedDateTime.now().plusSeconds(expiresInSeconds)
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
            url = getObjectUrl(objectName);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try {
            return new UploadFileFormData(
                url,
                bucketName,
                objectName,
                minioClient.getPresignedPostFormData(
                    formPolicy
                )
            );

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(FileKey key, byte[] file) {

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
