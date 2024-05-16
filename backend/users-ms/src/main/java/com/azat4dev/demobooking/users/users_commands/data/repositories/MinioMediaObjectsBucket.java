package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.FileExtension;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.FileKey;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectName;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectsBucket;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;

import java.net.URL;

@RequiredArgsConstructor
public class MinioMediaObjectsBucket implements MediaObjectsBucket {

    private final MinioClient minioClient;
    private final String bucketName;

    @Override
    public URL generateUploadUrl(MediaObjectName objectName, FileExtension fileExtension, int expiresInSeconds) {

        try {
            final var url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName.toString())
                    .method(Method.POST)
                    .expiry(expiresInSeconds)
                    .build()
            );

            return new URL(url);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(FileKey key, byte[] file) {

    }
}
