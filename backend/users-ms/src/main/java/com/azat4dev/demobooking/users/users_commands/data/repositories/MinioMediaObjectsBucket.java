package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.UploadFileFormData;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.FileKey;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectName;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectsBucket;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PostPolicy;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
public class MinioMediaObjectsBucket implements MediaObjectsBucket {

    private final URL baseUrl;
    private final MinioClient minioClient;
    private final String bucketName;

    public URL getObjectUrl(MediaObjectName objectName) throws MalformedURLException {
        var baseUrlString = baseUrl.toString();

        if (!baseUrlString.endsWith("/")) {
            baseUrlString = baseUrlString + "/";
        }

        return URI.create(baseUrlString + bucketName + "/" + objectName.toString()).toURL();
    }

    @Override
    public URL generateUploadUrl(MediaObjectName objectName, int expiresInSeconds) {

        try {
            final var url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
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
            bucketName,
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
}
