package com.azat4dev.booking.usersms.generated.server.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TokensPairDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-30T01:15:09.334912+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public class TokensPairDTO {

  private String access;

  private String refresh;

  public TokensPairDTO() {
    super();
  }

  public TokensPairDTO access(String access) {
    this.access = access;
    return this;
  }

  /**
   * Get access
   * @return access
  */
  @NotNull 
  @Schema(name = "access", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("access")
  public String getAccess() {
    return access;
  }

  public void setAccess(String access) {
    this.access = access;
  }

  public TokensPairDTO refresh(String refresh) {
    this.refresh = refresh;
    return this;
  }

  /**
   * Get refresh
   * @return refresh
  */
  @NotNull 
  @Schema(name = "refresh", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("refresh")
  public String getRefresh() {
    return refresh;
  }

  public void setRefresh(String refresh) {
    this.refresh = refresh;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TokensPairDTO tokensPairDTO = (TokensPairDTO) o;
    return Objects.equals(this.access, tokensPairDTO.access) &&
        Objects.equals(this.refresh, tokensPairDTO.refresh);
  }

  @Override
  public int hashCode() {
    return Objects.hash(access, refresh);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TokensPairDTO {\n");
    sb.append("    access: ").append(toIndentedString(access)).append("\n");
    sb.append("    refresh: ").append(toIndentedString(refresh)).append("\n");
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

