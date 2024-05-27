package com.azat4dev.booking.listingsms.queries.presentation.api.resources;

import com.azat4dev.booking.listingsms.queries.presentation.api.entities.GetListingPrivateDetailsResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private/listings")
public interface PrivateListingsQueriesResource {

    @GetMapping("/{listingId}")
    ResponseEntity<GetListingPrivateDetailsResponse> getPrivateListing(
        @PathVariable String listingId,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception;
}