package com.azat4dev.booking.users.users_queries.presentation.api.rest.resources.mappers;

import com.azat4dev.booking.users.users_queries.domain.entities.PersonalUserInfo;
import com.azat4dev.booking.usersms.generated.server.model.PersonalUserInfoDTO;

@FunctionalInterface
public interface MapPersonalUserInfoToDTO {

    PersonalUserInfoDTO map(PersonalUserInfo personalUserInfo);
}
