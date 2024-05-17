package com.azat4dev.demobooking.users.users_commands.domain.core.commands;

import com.azat4dev.demobooking.common.domain.event.Command;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.UploadedFileData;


public record UpdateUserPhoto(
    UserId userId,
    UploadedFileData uploadedFileData
) implements Command {

}
