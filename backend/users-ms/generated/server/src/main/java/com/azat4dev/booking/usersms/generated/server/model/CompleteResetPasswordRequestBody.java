package com.azat4dev.booking.usersms.generated.server.model;

import java.net.URI;
import java.util.Objects;
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
 * CompleteResetPasswordRequestBody
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-30T01:15:09.334912+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public class CompleteResetPasswordRequestBody {

  private UUID operationId;

  private String newPassword;

  private String resetPasswordToken;

  public CompleteResetPasswordRequestBody() {
    super();
  }

  public CompleteResetPasswordRequestBody operationId(UUID operationId) {
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

  public CompleteResetPasswordRequestBody newPassword(String newPassword) {
    this.newPassword = newPassword;
    return this;
  }

  /**
   * Get newPassword
   * @return newPassword
  */
  @NotNull @Size(min = 1) 
  @Schema(name = "newPassword", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newPassword")
  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public CompleteResetPasswordRequestBody resetPasswordToken(String resetPasswordToken) {
    this.resetPasswordToken = resetPasswordToken;
    return this;
  }

  /**
   * Get resetPasswordToken
   * @return resetPasswordToken
  */
  @NotNull @Size(min = 1) 
  @Schema(name = "resetPasswordToken", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resetPasswordToken")
  public String getResetPasswordToken() {
    return resetPasswordToken;
  }

  public void setResetPasswordToken(String resetPasswordToken) {
    this.resetPasswordToken = resetPasswordToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompleteResetPasswordRequestBody completeResetPasswordRequestBody = (CompleteResetPasswordRequestBody) o;
    return Objects.equals(this.operationId, completeResetPasswordRequestBody.operationId) &&
        Objects.equals(this.newPassword, completeResetPasswordRequestBody.newPassword) &&
        Objects.equals(this.resetPasswordToken, completeResetPasswordRequestBody.resetPasswordToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operationId, newPassword, resetPasswordToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompleteResetPasswordRequestBody {\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
    sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
    sb.append("    resetPasswordToken: ").append(toIndentedString(resetPasswordToken)).append("\n");
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

