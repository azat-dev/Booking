package com.azat4dev.booking.usersms.generated.events.dto;

import java.net.URI;
import java.util.Objects;
import com.azat4dev.booking.usersms.generated.events.dto.FullNameDTO;
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
 * UserSignedUpDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@JsonTypeName("UserSignedUp")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public class UserSignedUpDTO implements UsersDomainEventPayloadDTO {

  private Long createdAt;

  private String userId;

  private FullNameDTO fullName;

  private String email;

  private String emailVerificationStatus;

  public UserSignedUpDTO() {
    super();
  }

  public UserSignedUpDTO createdAt(Long createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
  */
  @NotNull 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  public UserSignedUpDTO userId(String userId) {
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

  public UserSignedUpDTO fullName(FullNameDTO fullName) {
    this.fullName = fullName;
    return this;
  }

  /**
   * Get fullName
   * @return fullName
  */
  @NotNull @Valid 
  @Schema(name = "fullName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fullName")
  public FullNameDTO getFullName() {
    return fullName;
  }

  public void setFullName(FullNameDTO fullName) {
    this.fullName = fullName;
  }

  public UserSignedUpDTO email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  */
  @NotNull 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserSignedUpDTO emailVerificationStatus(String emailVerificationStatus) {
    this.emailVerificationStatus = emailVerificationStatus;
    return this;
  }

  /**
   * Get emailVerificationStatus
   * @return emailVerificationStatus
  */
  @NotNull 
  @Schema(name = "emailVerificationStatus", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("emailVerificationStatus")
  public String getEmailVerificationStatus() {
    return emailVerificationStatus;
  }

  public void setEmailVerificationStatus(String emailVerificationStatus) {
    this.emailVerificationStatus = emailVerificationStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserSignedUpDTO userSignedUp = (UserSignedUpDTO) o;
    return Objects.equals(this.createdAt, userSignedUp.createdAt) &&
        Objects.equals(this.userId, userSignedUp.userId) &&
        Objects.equals(this.fullName, userSignedUp.fullName) &&
        Objects.equals(this.email, userSignedUp.email) &&
        Objects.equals(this.emailVerificationStatus, userSignedUp.emailVerificationStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(createdAt, userId, fullName, email, emailVerificationStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserSignedUpDTO {\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    emailVerificationStatus: ").append(toIndentedString(emailVerificationStatus)).append("\n");
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

