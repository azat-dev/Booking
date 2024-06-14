package com.azat4dev.booking.usersms.generated.events.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
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
 * UploadedFileFormDataDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@JsonTypeName("UploadedFileFormData")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public class UploadedFileFormDataDTO {

  private URI url;

  private String bucketName;

  private String objectName;

  @Valid
  private Map<String, String> formData = new HashMap<>();

  public UploadedFileFormDataDTO() {
    super();
  }

  public UploadedFileFormDataDTO url(URI url) {
    this.url = url;
    return this;
  }

  /**
   * Get url
   * @return url
  */
  @NotNull @Valid 
  @Schema(name = "url", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("url")
  public URI getUrl() {
    return url;
  }

  public void setUrl(URI url) {
    this.url = url;
  }

  public UploadedFileFormDataDTO bucketName(String bucketName) {
    this.bucketName = bucketName;
    return this;
  }

  /**
   * Get bucketName
   * @return bucketName
  */
  @NotNull 
  @Schema(name = "bucketName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("bucketName")
  public String getBucketName() {
    return bucketName;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }

  public UploadedFileFormDataDTO objectName(String objectName) {
    this.objectName = objectName;
    return this;
  }

  /**
   * Get objectName
   * @return objectName
  */
  @NotNull 
  @Schema(name = "objectName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("objectName")
  public String getObjectName() {
    return objectName;
  }

  public void setObjectName(String objectName) {
    this.objectName = objectName;
  }

  public UploadedFileFormDataDTO formData(Map<String, String> formData) {
    this.formData = formData;
    return this;
  }

  public UploadedFileFormDataDTO putFormDataItem(String key, String formDataItem) {
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
    UploadedFileFormDataDTO uploadedFileFormData = (UploadedFileFormDataDTO) o;
    return Objects.equals(this.url, uploadedFileFormData.url) &&
        Objects.equals(this.bucketName, uploadedFileFormData.bucketName) &&
        Objects.equals(this.objectName, uploadedFileFormData.objectName) &&
        Objects.equals(this.formData, uploadedFileFormData.formData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, bucketName, objectName, formData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UploadedFileFormDataDTO {\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    bucketName: ").append(toIndentedString(bucketName)).append("\n");
    sb.append("    objectName: ").append(toIndentedString(objectName)).append("\n");
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

