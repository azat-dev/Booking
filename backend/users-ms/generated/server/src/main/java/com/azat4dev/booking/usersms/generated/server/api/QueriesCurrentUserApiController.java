package com.azat4dev.booking.usersms.generated.server.api;

import com.azat4dev.booking.usersms.generated.server.model.PersonalUserInfoDTO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-30T01:15:09.334912+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
@Controller
@RequestMapping("${openapi.users.base-path:}")
public class QueriesCurrentUserApiController implements QueriesCurrentUserApi {

    private final QueriesCurrentUserApiDelegate delegate;

    public QueriesCurrentUserApiController(@Autowired(required = false) QueriesCurrentUserApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new QueriesCurrentUserApiDelegate() {});
    }

    @Override
    public QueriesCurrentUserApiDelegate getDelegate() {
        return delegate;
    }

}
