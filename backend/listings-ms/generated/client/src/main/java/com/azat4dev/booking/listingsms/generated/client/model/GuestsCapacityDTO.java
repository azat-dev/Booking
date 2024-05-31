/*
 * Listings API
 * Describes the API of Listings service
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.azat4dev.booking.listingsms.generated.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * GuestsCapacityDTO
 */
@JsonPropertyOrder({
  GuestsCapacityDTO.JSON_PROPERTY_ADULTS,
  GuestsCapacityDTO.JSON_PROPERTY_CHILDREN,
  GuestsCapacityDTO.JSON_PROPERTY_INFANTS
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-31T01:10:18.754538+03:00[Europe/Moscow]")
public class GuestsCapacityDTO {
  public static final String JSON_PROPERTY_ADULTS = "adults";
  private Integer adults;

  public static final String JSON_PROPERTY_CHILDREN = "children";
  private Integer children;

  public static final String JSON_PROPERTY_INFANTS = "infants";
  private Integer infants;

  public GuestsCapacityDTO() {
  }

  public GuestsCapacityDTO adults(Integer adults) {
    
    this.adults = adults;
    return this;
  }

   /**
   * Get adults
   * minimum: 1
   * maximum: 16
   * @return adults
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ADULTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getAdults() {
    return adults;
  }


  @JsonProperty(JSON_PROPERTY_ADULTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
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
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_CHILDREN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getChildren() {
    return children;
  }


  @JsonProperty(JSON_PROPERTY_CHILDREN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
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
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_INFANTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getInfants() {
    return infants;
  }


  @JsonProperty(JSON_PROPERTY_INFANTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
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
