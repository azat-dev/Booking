package com.azat4dev.booking.users.queries.infrastructure.presentation.api.rest.mappers;

import com.azat4dev.booking.users.queries.domain.entities.PersonalUserInfo;
import com.azat4dev.booking.usersms.generated.server.model.PersonalUserInfoDTO;

@FunctionalInterface
public interface MapPersonalUserInfoToDTO {

    PersonalUserInfoDTO map(PersonalUserInfo personalUserInfo);
}
