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
 * AddListingRequestBody
 */
@lombok.Builder(toBuilder = true)
@lombok.RequiredArgsConstructor
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-28T23:07:12.290925+03:00[Europe/Moscow]")
public class AddListingRequestBody {

  private UUID operationId;

  private String title;

  public AddListingRequestBody operationId(UUID operationId) {
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

  public AddListingRequestBody title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
  */
  @NotNull 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddListingRequestBody addListingRequestBody = (AddListingRequestBody) o;
    return Objects.equals(this.operationId, addListingRequestBody.operationId) &&
        Objects.equals(this.title, addListingRequestBody.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operationId, title);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddListingRequestBody {\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
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

