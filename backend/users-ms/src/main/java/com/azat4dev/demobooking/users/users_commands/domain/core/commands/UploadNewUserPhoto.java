package com.azat4dev.demobooking.users.users_commands.domain.core.commands;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.domain.event.Command;
import com.azat4dev.demobooking.common.utils.Assert;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.InitialUserPhotoFileName;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UploadNewUserPhoto implements Command {

    public static final long MAX_SIZE = 1024 * 1024;

    private final UserId userId;
    private final InitialUserPhotoFileName fileName;
    private final byte[] photo;

    public static UploadNewUserPhoto checkAndMakeFrom(
        UserId userId,
        long size,
        InitialUserPhotoFileName fileName,
        Supplier<byte[]> photoSupplier
    ) throws MaxSizeException {
        Assert.isTrue(size <= MAX_SIZE, MaxSizeException::new);
        return new UploadNewUserPhoto(userId, fileName, photoSupplier.get());
    }

    public static UploadNewUserPhoto dangerouslyMake(UserId userId, InitialUserPhotoFileName fileName, byte[] photo) {
        return new UploadNewUserPhoto(userId, fileName, photo);
    }

    public static final class MaxSizeException extends DomainException {
        public MaxSizeException() {
            super("Photo size should be less than 1MB");
        }

        @Override
        public String getCode() {
            return "MaxSize";
        }
    }
}
