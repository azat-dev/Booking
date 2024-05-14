package com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories;

public interface FilesRepository {

    void put(FileKey key, byte[] file);
}
