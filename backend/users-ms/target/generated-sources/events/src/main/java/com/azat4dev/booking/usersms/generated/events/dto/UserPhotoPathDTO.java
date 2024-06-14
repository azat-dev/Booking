package com.azat4dev.booking.usersms.generated.events.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserPhotoPathDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@JsonTypeName("UserPhotoPath")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public class UserPhotoPathDTO {

  private String bucketName;

  private String objectName;

  public UserPhotoPathDTO() {
    super();
  }

  public UserPhotoPathDTO bucketName(String bucketName) {
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

  public UserPhotoPathDTO objectName(String objectName) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserPhotoPathDTO userPhotoPath = (UserPhotoPathDTO) o;
    return Objects.equals(this.bucketName, userPhotoPath.bucketName) &&
        Objects.equals(this.objectName, userPhotoPath.objectName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bucketName, objectName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserPhotoPathDTO {\n");
    sb.append("    bucketName: ").append(toIndentedString(bucketName)).append("\n");
    sb.append("    objectName: ").append(toIndentedString(objectName)).append("\n");
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

