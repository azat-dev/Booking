package com.azat4dev.booking.usersms.generated.events.dto;

import java.net.URI;
import java.util.Objects;
import com.azat4dev.booking.usersms.generated.events.dto.UploadedFileDataDTO;
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
 * FailedUpdateUserPhotoDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@JsonTypeName("FailedUpdateUserPhoto")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public class FailedUpdateUserPhotoDTO implements UsersDomainEventPayloadDTO {

  private String operationId;

  private String userId;

  private UploadedFileDataDTO uploadedFileData;

  public FailedUpdateUserPhotoDTO() {
    super();
  }

  public FailedUpdateUserPhotoDTO operationId(String operationId) {
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

  public FailedUpdateUserPhotoDTO userId(String userId) {
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

  public FailedUpdateUserPhotoDTO uploadedFileData(UploadedFileDataDTO uploadedFileData) {
    this.uploadedFileData = uploadedFileData;
    return this;
  }

  /**
   * Get uploadedFileData
   * @return uploadedFileData
  */
  @NotNull @Valid 
  @Schema(name = "uploadedFileData", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("uploadedFileData")
  public UploadedFileDataDTO getUploadedFileData() {
    return uploadedFileData;
  }

  public void setUploadedFileData(UploadedFileDataDTO uploadedFileData) {
    this.uploadedFileData = uploadedFileData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FailedUpdateUserPhotoDTO failedUpdateUserPhoto = (FailedUpdateUserPhotoDTO) o;
    return Objects.equals(this.operationId, failedUpdateUserPhoto.operationId) &&
        Objects.equals(this.userId, failedUpdateUserPhoto.userId) &&
        Objects.equals(this.uploadedFileData, failedUpdateUserPhoto.uploadedFileData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operationId, userId, uploadedFileData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FailedUpdateUserPhotoDTO {\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    uploadedFileData: ").append(toIndentedString(uploadedFileData)).append("\n");
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

