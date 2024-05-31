package com.azat4dev.booking.listingsms.generated.server.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets PropertyTypeDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-06-01T00:08:26.456416+03:00[Europe/Moscow]")
public enum PropertyTypeDTO {
  
  APARTMENT("APARTMENT"),
  
  HOUSE("HOUSE"),
  
  VILLA("VILLA"),
  
  CABIN("CABIN"),
  
  COTTAGE("COTTAGE"),
  
  CHALET("CHALET"),
  
  BUNGALOW("BUNGALOW"),
  
  TOWNHOUSE("TOWNHOUSE"),
  
  GUESTHOUSE("GUESTHOUSE"),
  
  LOFT("LOFT"),
  
  HOSTEL("HOSTEL"),
  
  BOAT("BOAT"),
  
  BEDANDBREAKFAST("BEDANDBREAKFAST"),
  
  OTHER("OTHER");

  private String value;

  PropertyTypeDTO(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static PropertyTypeDTO fromValue(String value) {
    for (PropertyTypeDTO b : PropertyTypeDTO.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

