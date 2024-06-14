package com.azat4dev.booking.usersms.generated.events.dto;

import java.net.URI;
import java.util.Objects;
import com.azat4dev.booking.usersms.generated.events.dto.UploadedFileFormDataDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GeneratedUserPhotoUploadUrlDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@JsonTypeName("GeneratedUserPhotoUploadUrl")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public class GeneratedUserPhotoUploadUrlDTO implements UsersDomainEventPayloadDTO {

  private String userId;

  private UploadedFileFormDataDTO formData;

  public GeneratedUserPhotoUploadUrlDTO() {
    super();
  }

  public GeneratedUserPhotoUploadUrlDTO userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
  */
  @NotNull 
  @Schema(name = "userId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("userId")
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public GeneratedUserPhotoUploadUrlDTO formData(UploadedFileFormDataDTO formData) {
    this.formData = formData;
    return this;
  }

  /**
   * Get formData
   * @return formData
  */
  @NotNull @Valid 
  @Schema(name = "formData", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("formData")
  public UploadedFileFormDataDTO getFormData() {
    return formData;
  }

  public void setFormData(UploadedFileFormDataDTO formData) {
    this.formData = formData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeneratedUserPhotoUploadUrlDTO generatedUserPhotoUploadUrl = (GeneratedUserPhotoUploadUrlDTO) o;
    return Objects.equals(this.userId, generatedUserPhotoUploadUrl.userId) &&
        Objects.equals(this.formData, generatedUserPhotoUploadUrl.formData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, formData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeneratedUserPhotoUploadUrlDTO {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    formData: ").append(toIndentedString(formData)).append("\n");
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

