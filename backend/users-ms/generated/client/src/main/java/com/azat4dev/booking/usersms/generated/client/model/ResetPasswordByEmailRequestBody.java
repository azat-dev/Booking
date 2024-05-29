/*
 * Users  API
 * Describes the API of Users Endpoint
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.azat4dev.booking.usersms.generated.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * ResetPasswordByEmailRequestBody
 */
@JsonPropertyOrder({
  ResetPasswordByEmailRequestBody.JSON_PROPERTY_OPERATION_ID,
  ResetPasswordByEmailRequestBody.JSON_PROPERTY_EMAIL
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-30T01:15:08.126842+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public class ResetPasswordByEmailRequestBody {
  public static final String JSON_PROPERTY_OPERATION_ID = "operationId";
  private UUID operationId;

  public static final String JSON_PROPERTY_EMAIL = "email";
  private String email;

  public ResetPasswordByEmailRequestBody() {
  }

  public ResetPasswordByEmailRequestBody operationId(UUID operationId) {
    
    this.operationId = operationId;
    return this;
  }

   /**
   * Get operationId
   * @return operationId
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_OPERATION_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getOperationId() {
    return operationId;
  }


  @JsonProperty(JSON_PROPERTY_OPERATION_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setOperationId(UUID operationId) {
    this.operationId = operationId;
  }


  public ResetPasswordByEmailRequestBody email(String email) {
    
    this.email = email;
    return this;
  }

   /**
   * Get email
   * @return email
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EMAIL)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getEmail() {
    return email;
  }


  @JsonProperty(JSON_PROPERTY_EMAIL)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEmail(String email) {
    this.email = email;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResetPasswordByEmailRequestBody resetPasswordByEmailRequestBody = (ResetPasswordByEmailRequestBody) o;
    return Objects.equals(this.operationId, resetPasswordByEmailRequestBody.operationId) &&
        Objects.equals(this.email, resetPasswordByEmailRequestBody.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operationId, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResetPasswordByEmailRequestBody {\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
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

