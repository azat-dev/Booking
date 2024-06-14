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
 * GenerateUserPhotoUploadUrlDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@JsonTypeName("GenerateUserPhotoUploadUrl")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public class GenerateUserPhotoUploadUrlDTO {

  private String userId;

  private String fileExtension;

  private Integer fileSize;

  private String operationId;

  private Long requestedAt;

  public GenerateUserPhotoUploadUrlDTO() {
    super();
  }

  public GenerateUserPhotoUploadUrlDTO userId(String userId) {
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

  public GenerateUserPhotoUploadUrlDTO fileExtension(String fileExtension) {
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

  public GenerateUserPhotoUploadUrlDTO fileSize(Integer fileSize) {
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

  public GenerateUserPhotoUploadUrlDTO operationId(String operationId) {
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

  public GenerateUserPhotoUploadUrlDTO requestedAt(Long requestedAt) {
    this.requestedAt = requestedAt;
    return this;
  }

  /**
   * Get requestedAt
   * @return requestedAt
  */
  @NotNull 
  @Schema(name = "requestedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("requestedAt")
  public Long getRequestedAt() {
    return requestedAt;
  }

  public void setRequestedAt(Long requestedAt) {
    this.requestedAt = requestedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateUserPhotoUploadUrlDTO generateUserPhotoUploadUrl = (GenerateUserPhotoUploadUrlDTO) o;
    return Objects.equals(this.userId, generateUserPhotoUploadUrl.userId) &&
        Objects.equals(this.fileExtension, generateUserPhotoUploadUrl.fileExtension) &&
        Objects.equals(this.fileSize, generateUserPhotoUploadUrl.fileSize) &&
        Objects.equals(this.operationId, generateUserPhotoUploadUrl.operationId) &&
        Objects.equals(this.requestedAt, generateUserPhotoUploadUrl.requestedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, fileExtension, fileSize, operationId, requestedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateUserPhotoUploadUrlDTO {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    fileExtension: ").append(toIndentedString(fileExtension)).append("\n");
    sb.append("    fileSize: ").append(toIndentedString(fileSize)).append("\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
    sb.append("    requestedAt: ").append(toIndentedString(requestedAt)).append("\n");
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

