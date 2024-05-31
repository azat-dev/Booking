package com.azat4dev.booking.listingsms.generated.server.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AddListingResponse
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-06-01T00:08:26.456416+03:00[Europe/Moscow]")
public class AddListingResponse {

  private UUID listingId;

  public AddListingResponse listingId(UUID listingId) {
    this.listingId = listingId;
    return this;
  }

  /**
   * Get listingId
   * @return listingId
  */
  @NotNull @Valid 
  @Schema(name = "listingId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("listingId")
  public UUID getListingId() {
    return listingId;
  }

  public void setListingId(UUID listingId) {
    this.listingId = listingId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddListingResponse addListingResponse = (AddListingResponse) o;
    return Objects.equals(this.listingId, addListingResponse.listingId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(listingId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddListingResponse {\n");
    sb.append("    listingId: ").append(toIndentedString(listingId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

