package com.azat4dev.booking.listingsms.generated.server.api;

import com.azat4dev.booking.listingsms.generated.server.model.AddListingRequestBody;
import com.azat4dev.booking.listingsms.generated.server.model.AddListingResponse;
import com.azat4dev.booking.listingsms.generated.server.model.GetPhotoUploadUrlForListingResponse;
import java.util.UUID;


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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-28T23:07:12.290925+03:00[Europe/Moscow]")
@Controller
@RequestMapping("${openapi.listings.base-path:}")
public class CommandsApiController implements CommandsApi {

    private final CommandsApiDelegate delegate;

    public CommandsApiController(@Autowired(required = false) CommandsApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new CommandsApiDelegate() {});
    }

    @Override
    public CommandsApiDelegate getDelegate() {
        return delegate;
    }

}
