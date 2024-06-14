package com.azat4dev.booking.usersms.generated.events.dto;

import java.net.URI;
import java.util.Objects;
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
 * FailedGenerateUserPhotoUploadUrlDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@JsonTypeName("FailedGenerateUserPhotoUploadUrl")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public class FailedGenerateUserPhotoUploadUrlDTO implements UsersDomainEventPayloadDTO {

  private String userId;

  private String fileExtension;

  private Integer fileSize;

  private String operationId;

  public FailedGenerateUserPhotoUploadUrlDTO() {
    super();
  }

  public FailedGenerateUserPhotoUploadUrlDTO userId(String userId) {
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

  public FailedGenerateUserPhotoUploadUrlDTO fileExtension(String fileExtension) {
    this.fileExtension = fileExtension;
    return this;
  }

  /**
   * Get fileExtension
   * @return fileExtension
  */
  @NotNull 
  @Schema(name = "fileExtension", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fileExtension")
  public String getFileExtension() {
    return fileExtension;
  }

  public void setFileExtension(String fileExtension) {
    this.fileExtension = fileExtension;
  }

  public FailedGenerateUserPhotoUploadUrlDTO fileSize(Integer fileSize) {
    this.fileSize = fileSize;
    return this;
  }

  /**
   * Get fileSize
   * @return fileSize
  */
  @NotNull 
  @Schema(name = "fileSize", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fileSize")
  public Integer getFileSize() {
    return fileSize;
  }

  public void setFileSize(Integer fileSize) {
    this.fileSize = fileSize;
  }

  public FailedGenerateUserPhotoUploadUrlDTO operationId(String operationId) {
    this.operationId = operationId;
    return this;
  }

  /**
   * Get operationId
   * @return operationId
  */
  @NotNull 
  @Schema(name = "operationId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("operationId")
  public String getOperationId() {
    return operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FailedGenerateUserPhotoUploadUrlDTO failedGenerateUserPhotoUploadUrl = (FailedGenerateUserPhotoUploadUrlDTO) o;
    return Objects.equals(this.userId, failedGenerateUserPhotoUploadUrl.userId) &&
        Objects.equals(this.fileExtension, failedGenerateUserPhotoUploadUrl.fileExtension) &&
        Objects.equals(this.fileSize, failedGenerateUserPhotoUploadUrl.fileSize) &&
        Objects.equals(this.operationId, failedGenerateUserPhotoUploadUrl.operationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, fileExtension, fileSize, operationId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FailedGenerateUserPhotoUploadUrlDTO {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    fileExtension: ").append(toIndentedString(fileExtension)).append("\n");
    sb.append("    fileSize: ").append(toIndentedString(fileSize)).append("\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
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

