package com.azat4dev.booking.usersms.generated.server.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GenerateUploadUserPhotoUrlRequestBodyDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@JsonTypeName("GenerateUploadUserPhotoUrlRequestBody")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public class GenerateUploadUserPhotoUrlRequestBodyDTO {

  private UUID operationId;

  private String fileName;

  private String fileExtension;

  private Integer fileSize;

  public GenerateUploadUserPhotoUrlRequestBodyDTO() {
    super();
  }

  public GenerateUploadUserPhotoUrlRequestBodyDTO operationId(UUID operationId) {
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

  public GenerateUploadUserPhotoUrlRequestBodyDTO fileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  /**
   * Get fileName
   * @return fileName
  */
  @NotNull 
  @Schema(name = "fileName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fileName")
  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public GenerateUploadUserPhotoUrlRequestBodyDTO fileExtension(String fileExtension) {
    this.fileExtension = fileExtension;
    return this;
  }

  /**
   * Get fileExtension
   * @return fileExtension
  */
  @NotNull @Size(max = 10) 
  @Schema(name = "fileExtension", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fileExtension")
  public String getFileExtension() {
    return fileExtension;
  }

  public void setFileExtension(String fileExtension) {
    this.fileExtension = fileExtension;
  }

  public GenerateUploadUserPhotoUrlRequestBodyDTO fileSize(Integer fileSize) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateUploadUserPhotoUrlRequestBodyDTO generateUploadUserPhotoUrlRequestBody = (GenerateUploadUserPhotoUrlRequestBodyDTO) o;
    return Objects.equals(this.operationId, generateUploadUserPhotoUrlRequestBody.operationId) &&
        Objects.equals(this.fileName, generateUploadUserPhotoUrlRequestBody.fileName) &&
        Objects.equals(this.fileExtension, generateUploadUserPhotoUrlRequestBody.fileExtension) &&
        Objects.equals(this.fileSize, generateUploadUserPhotoUrlRequestBody.fileSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operationId, fileName, fileExtension, fileSize);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateUploadUserPhotoUrlRequestBodyDTO {\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
    sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
    sb.append("    fileExtension: ").append(toIndentedString(fileExtension)).append("\n");
    sb.append("    fileSize: ").append(toIndentedString(fileSize)).append("\n");
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

