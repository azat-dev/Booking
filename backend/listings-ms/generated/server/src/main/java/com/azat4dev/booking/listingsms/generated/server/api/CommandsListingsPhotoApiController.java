package com.azat4dev.booking.listingsms.generated.server.api;

import com.azat4dev.booking.listingsms.generated.server.model.AddListingPhotoRequestBody;
import com.azat4dev.booking.listingsms.generated.server.model.AddListingPhotoResponseBody;
import com.azat4dev.booking.listingsms.generated.server.model.GenerateUploadListingPhotoUrlRequestBody;
import com.azat4dev.booking.listingsms.generated.server.model.GenerateUploadListingPhotoUrlResponseBody;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-06-01T00:08:26.456416+03:00[Europe/Moscow]")
@Controller
@RequestMapping("${openapi.listings.base-path:}")
public class CommandsListingsPhotoApiController implements CommandsListingsPhotoApi {

    private final CommandsListingsPhotoApiDelegate delegate;

    public CommandsListingsPhotoApiController(@Autowired(required = false) CommandsListingsPhotoApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new CommandsListingsPhotoApiDelegate() {});
    }

    @Override
    public CommandsListingsPhotoApiDelegate getDelegate() {
        return delegate;
    }

}
