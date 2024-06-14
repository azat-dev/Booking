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
 * CompleteEmailVerificationDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@JsonTypeName("CompleteEmailVerification")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public class CompleteEmailVerificationDTO implements UsersDomainEventPayloadDTO {

  private String emailVerificationToken;

  public CompleteEmailVerificationDTO() {
    super();
  }

  public CompleteEmailVerificationDTO emailVerificationToken(String emailVerificationToken) {
    this.emailVerificationToken = emailVerificationToken;
    return this;
  }

  /**
   * Get emailVerificationToken
   * @return emailVerificationToken
  */
  @NotNull 
  @Schema(name = "emailVerificationToken", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("emailVerificationToken")
  public String getEmailVerificationToken() {
    return emailVerificationToken;
  }

  public void setEmailVerificationToken(String emailVerificationToken) {
    this.emailVerificationToken = emailVerificationToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompleteEmailVerificationDTO completeEmailVerification = (CompleteEmailVerificationDTO) o;
    return Objects.equals(this.emailVerificationToken, completeEmailVerification.emailVerificationToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(emailVerificationToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompleteEmailVerificationDTO {\n");
    sb.append("    emailVerificationToken: ").append(toIndentedString(emailVerificationToken)).append("\n");
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

