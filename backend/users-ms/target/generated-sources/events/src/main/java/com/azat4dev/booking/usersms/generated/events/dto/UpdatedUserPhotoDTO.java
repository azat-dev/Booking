package com.azat4dev.booking.usersms.generated.events.dto;

import java.net.URI;
import java.util.Objects;
import com.azat4dev.booking.usersms.generated.events.dto.UserPhotoPathDTO;
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
 * UpdatedUserPhotoDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@JsonTypeName("UpdatedUserPhoto")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public class UpdatedUserPhotoDTO implements UsersDomainEventPayloadDTO {

  private String userId;

  private UserPhotoPathDTO newPhotoPath;

  private Optional<UserPhotoPathDTO> prevPhotoPath = Optional.empty();

  public UpdatedUserPhotoDTO() {
    super();
  }

  public UpdatedUserPhotoDTO userId(String userId) {
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

  public UpdatedUserPhotoDTO newPhotoPath(UserPhotoPathDTO newPhotoPath) {
    this.newPhotoPath = newPhotoPath;
    return this;
  }

  /**
   * Get newPhotoPath
   * @return newPhotoPath
  */
  @NotNull @Valid 
  @Schema(name = "newPhotoPath", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newPhotoPath")
  public UserPhotoPathDTO getNewPhotoPath() {
    return newPhotoPath;
  }

  public void setNewPhotoPath(UserPhotoPathDTO newPhotoPath) {
    this.newPhotoPath = newPhotoPath;
  }

  public UpdatedUserPhotoDTO prevPhotoPath(UserPhotoPathDTO prevPhotoPath) {
    this.prevPhotoPath = Optional.of(prevPhotoPath);
    return this;
  }

  /**
   * Get prevPhotoPath
   * @return prevPhotoPath
  */
  @Valid 
  @Schema(name = "prevPhotoPath", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prevPhotoPath")
  public Optional<UserPhotoPathDTO> getPrevPhotoPath() {
    return prevPhotoPath;
  }

  public void setPrevPhotoPath(Optional<UserPhotoPathDTO> prevPhotoPath) {
    this.prevPhotoPath = prevPhotoPath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdatedUserPhotoDTO updatedUserPhoto = (UpdatedUserPhotoDTO) o;
    return Objects.equals(this.userId, updatedUserPhoto.userId) &&
        Objects.equals(this.newPhotoPath, updatedUserPhoto.newPhotoPath) &&
        Objects.equals(this.prevPhotoPath, updatedUserPhoto.prevPhotoPath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, newPhotoPath, prevPhotoPath);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdatedUserPhotoDTO {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    newPhotoPath: ").append(toIndentedString(newPhotoPath)).append("\n");
    sb.append("    prevPhotoPath: ").append(toIndentedString(prevPhotoPath)).append("\n");
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

