package com.azat4dev.booking.usersms.generated.server.model;

import java.net.URI;
import java.util.Objects;
import com.azat4dev.booking.usersms.generated.server.model.TokensPairDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SignUpByEmailResponseBody
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-30T09:25:21.695569+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public class SignUpByEmailResponseBody {

  private UUID userId;

  private TokensPairDTO tokens;

  public SignUpByEmailResponseBody() {
    super();
  }

  public SignUpByEmailResponseBody userId(UUID userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
  */
  @NotNull @Valid 
  @Schema(name = "userId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("userId")
  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public SignUpByEmailResponseBody tokens(TokensPairDTO tokens) {
    this.tokens = tokens;
    return this;
  }

  /**
   * Get tokens
   * @return tokens
  */
  @NotNull @Valid 
  @Schema(name = "tokens", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tokens")
  public TokensPairDTO getTokens() {
    return tokens;
  }

  public void setTokens(TokensPairDTO tokens) {
    this.tokens = tokens;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SignUpByEmailResponseBody signUpByEmailResponseBody = (SignUpByEmailResponseBody) o;
    return Objects.equals(this.userId, signUpByEmailResponseBody.userId) &&
        Objects.equals(this.tokens, signUpByEmailResponseBody.tokens);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, tokens);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SignUpByEmailResponseBody {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    tokens: ").append(toIndentedString(tokens)).append("\n");
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

