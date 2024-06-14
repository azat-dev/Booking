package com.azat4dev.booking.usersms.generated.server.model;

import java.net.URI;
import java.util.Objects;
import com.azat4dev.booking.usersms.generated.server.model.UploadedFileDataDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.HashMap;
import java.util.Map;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GenerateUploadUserPhotoUrlResponseBodyDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@JsonTypeName("GenerateUploadUserPhotoUrlResponseBody")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public class GenerateUploadUserPhotoUrlResponseBodyDTO {

  private UploadedFileDataDTO objectPath;

  @Valid
  private Map<String, String> formData = new HashMap<>();

  public GenerateUploadUserPhotoUrlResponseBodyDTO() {
    super();
  }

  public GenerateUploadUserPhotoUrlResponseBodyDTO objectPath(UploadedFileDataDTO objectPath) {
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

  public GenerateUploadUserPhotoUrlResponseBodyDTO formData(Map<String, String> formData) {
    this.formData = formData;
    return this;
  }

  public GenerateUploadUserPhotoUrlResponseBodyDTO putFormDataItem(String key, String formDataItem) {
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
    GenerateUploadUserPhotoUrlResponseBodyDTO generateUploadUserPhotoUrlResponseBody = (GenerateUploadUserPhotoUrlResponseBodyDTO) o;
    return Objects.equals(this.objectPath, generateUploadUserPhotoUrlResponseBody.objectPath) &&
        Objects.equals(this.formData, generateUploadUserPhotoUrlResponseBody.formData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectPath, formData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateUploadUserPhotoUrlResponseBodyDTO {\n");
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

