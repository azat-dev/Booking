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
 * AddListingPhotoResponseBody
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-06-01T00:08:26.456416+03:00[Europe/Moscow]")
public class AddListingPhotoResponseBody {

  private UUID operationId;

  private UUID listingPhotoId;

  public AddListingPhotoResponseBody operationId(UUID operationId) {
    this.operationId = operationId;
    return this;
  }

  /**
   * Get operationId
   * @return operationId
  */
  @NotNull @Valid 
  @Schema(name = "operationId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("operationId")
  public UUID getOperationId() {
    return operationId;
  }

  public void setOperationId(UUID operationId) {
    this.operationId = operationId;
  }

  public AddListingPhotoResponseBody listingPhotoId(UUID listingPhotoId) {
    this.listingPhotoId = listingPhotoId;
    return this;
  }

  /**
   * Get listingPhotoId
   * @return listingPhotoId
  */
  @NotNull @Valid 
  @Schema(name = "listingPhotoId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("listingPhotoId")
  public UUID getListingPhotoId() {
    return listingPhotoId;
  }

  public void setListingPhotoId(UUID listingPhotoId) {
    this.listingPhotoId = listingPhotoId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddListingPhotoResponseBody addListingPhotoResponseBody = (AddListingPhotoResponseBody) o;
    return Objects.equals(this.operationId, addListingPhotoResponseBody.operationId) &&
        Objects.equals(this.listingPhotoId, addListingPhotoResponseBody.listingPhotoId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operationId, listingPhotoId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddListingPhotoResponseBody {\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
    sb.append("    listingPhotoId: ").append(toIndentedString(listingPhotoId)).append("\n");
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

