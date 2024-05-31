package com.azat4dev.booking.listingsms.generated.server.model;

import java.net.URI;
import java.util.Objects;
import com.azat4dev.booking.listingsms.generated.server.model.UploadedFileDataDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GenerateUploadListingPhotoUrlResponseBody
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-06-01T00:08:26.456416+03:00[Europe/Moscow]")
public class GenerateUploadListingPhotoUrlResponseBody {

  private UploadedFileDataDTO objectPath;

  @Valid
  private Map<String, String> formData = new HashMap<>();

  public GenerateUploadListingPhotoUrlResponseBody objectPath(UploadedFileDataDTO objectPath) {
    this.objectPath = objectPath;
    return this;
  }

  /**
   * Get objectPath
   * @return objectPath
  */
  @NotNull @Valid 
  @Schema(name = "objectPath", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("objectPath")
  public UploadedFileDataDTO getObjectPath() {
    return objectPath;
  }

  public void setObjectPath(UploadedFileDataDTO objectPath) {
    this.objectPath = objectPath;
  }

  public GenerateUploadListingPhotoUrlResponseBody formData(Map<String, String> formData) {
    this.formData = formData;
    return this;
  }

  public GenerateUploadListingPhotoUrlResponseBody putFormDataItem(String key, String formDataItem) {
    if (this.formData == null) {
      this.formData = new HashMap<>();
    }
    this.formData.put(key, formDataItem);
    return this;
  }

  /**
   * Get formData
   * @return formData
  */
  @NotNull 
  @Schema(name = "formData", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("formData")
  public Map<String, String> getFormData() {
    return formData;
  }

  public void setFormData(Map<String, String> formData) {
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
    GenerateUploadListingPhotoUrlResponseBody generateUploadListingPhotoUrlResponseBody = (GenerateUploadListingPhotoUrlResponseBody) o;
    return Objects.equals(this.objectPath, generateUploadListingPhotoUrlResponseBody.objectPath) &&
        Objects.equals(this.formData, generateUploadListingPhotoUrlResponseBody.formData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectPath, formData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateUploadListingPhotoUrlResponseBody {\n");
    sb.append("    objectPath: ").append(toIndentedString(objectPath)).append("\n");
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

