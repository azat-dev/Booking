package com.azat4dev.booking.listingsms.generated.server.model;

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
 * GuestsCapacityDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-06-01T00:08:26.456416+03:00[Europe/Moscow]")
public class GuestsCapacityDTO {

  private Integer adults;

  private Integer children;

  private Integer infants;

  public GuestsCapacityDTO adults(Integer adults) {
    this.adults = adults;
    return this;
  }

  /**
   * Get adults
   * minimum: 1
   * maximum: 16
   * @return adults
  */
  @NotNull @Min(1) @Max(16) 
  @Schema(name = "adults", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("adults")
  public Integer getAdults() {
    return adults;
  }

  public void setAdults(Integer adults) {
    this.adults = adults;
  }

  public GuestsCapacityDTO children(Integer children) {
    this.children = children;
    return this;
  }

  /**
   * Get children
   * minimum: 0
   * maximum: 5
   * @return children
  */
  @NotNull @Min(0) @Max(5) 
  @Schema(name = "children", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("children")
  public Integer getChildren() {
    return children;
  }

  public void setChildren(Integer children) {
    this.children = children;
  }

  public GuestsCapacityDTO infants(Integer infants) {
    this.infants = infants;
    return this;
  }

  /**
   * Get infants
   * minimum: 0
   * maximum: 5
   * @return infants
  */
  @NotNull @Min(0) @Max(5) 
  @Schema(name = "infants", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("infants")
  public Integer getInfants() {
    return infants;
  }

  public void setInfants(Integer infants) {
    this.infants = infants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuestsCapacityDTO guestsCapacityDTO = (GuestsCapacityDTO) o;
    return Objects.equals(this.adults, guestsCapacityDTO.adults) &&
        Objects.equals(this.children, guestsCapacityDTO.children) &&
        Objects.equals(this.infants, guestsCapacityDTO.infants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adults, children, infants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuestsCapacityDTO {\n");
    sb.append("    adults: ").append(toIndentedString(adults)).append("\n");
    sb.append("    children: ").append(toIndentedString(children)).append("\n");
    sb.append("    infants: ").append(toIndentedString(infants)).append("\n");
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

