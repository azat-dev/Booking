package com.azat4dev.booking.listingsms.commands.api.resources;

import com.azat4dev.booking.listingsms.commands.api.dto.AddListingRequest;
import com.azat4dev.booking.listingsms.commands.api.dto.AddListingResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private/listings")
public interface ListingsResource {

    @PostMapping
    ResponseEntity<AddListingResponse> addNew(
        @RequestBody AddListingRequest body,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception;
}