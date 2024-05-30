package com.azat4dev.booking.usersms.generated.server.model;

import java.net.URI;
import java.util.Objects;
import com.azat4dev.booking.usersms.generated.server.model.FullNameDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SignUpByEmailRequestBody
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-30T09:25:21.695569+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public class SignUpByEmailRequestBody {

  private String email;

  private String password;

  private FullNameDTO fullName;

  public SignUpByEmailRequestBody() {
    super();
  }

  public SignUpByEmailRequestBody email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  */
  @NotNull @jakarta.validation.constraints.Email 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public SignUpByEmailRequestBody password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
  */
  @NotNull 
  @Schema(name = "password", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public SignUpByEmailRequestBody fullName(FullNameDTO fullName) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SignUpByEmailRequestBody signUpByEmailRequestBody = (SignUpByEmailRequestBody) o;
    return Objects.equals(this.email, signUpByEmailRequestBody.email) &&
        Objects.equals(this.password, signUpByEmailRequestBody.password) &&
        Objects.equals(this.fullName, signUpByEmailRequestBody.fullName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, password, fullName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SignUpByEmailRequestBody {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
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

