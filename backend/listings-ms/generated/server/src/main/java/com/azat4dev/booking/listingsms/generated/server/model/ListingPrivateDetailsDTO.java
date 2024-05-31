package com.azat4dev.booking.listingsms.generated.server.model;

import java.net.URI;
import java.util.Objects;
import com.azat4dev.booking.listingsms.generated.server.model.AddressDTO;
import com.azat4dev.booking.listingsms.generated.server.model.GuestsCapacityDTO;
import com.azat4dev.booking.listingsms.generated.server.model.ListingStatusDTO;
import com.azat4dev.booking.listingsms.generated.server.model.PropertyTypeDTO;
import com.azat4dev.booking.listingsms.generated.server.model.RoomTypeDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ListingPrivateDetailsDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-31T01:10:19.057492+03:00[Europe/Moscow]")
public class ListingPrivateDetailsDTO {

  private UUID id;

  private String title;

  private ListingStatusDTO status;

  private String description;

  private GuestsCapacityDTO guestCapacity;

  private PropertyTypeDTO propertyType;

  private RoomTypeDTO roomType;

  private AddressDTO address;

  private Object location;

  public ListingPrivateDetailsDTO id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @NotNull @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public ListingPrivateDetailsDTO title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
  */
  @NotNull 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ListingPrivateDetailsDTO status(ListingStatusDTO status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  @NotNull @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public ListingStatusDTO getStatus() {
    return status;
  }

  public void setStatus(ListingStatusDTO status) {
    this.status = status;
  }

  public ListingPrivateDetailsDTO description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ListingPrivateDetailsDTO guestCapacity(GuestsCapacityDTO guestCapacity) {
    this.guestCapacity = guestCapacity;
    return this;
  }

  /**
   * Get guestCapacity
   * @return guestCapacity
  */
  @Valid 
  @Schema(name = "guestCapacity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guestCapacity")
  public GuestsCapacityDTO getGuestCapacity() {
    return guestCapacity;
  }

  public void setGuestCapacity(GuestsCapacityDTO guestCapacity) {
    this.guestCapacity = guestCapacity;
  }

  public ListingPrivateDetailsDTO propertyType(PropertyTypeDTO propertyType) {
    this.propertyType = propertyType;
    return this;
  }

  /**
   * Get propertyType
   * @return propertyType
  */
  @Valid 
  @Schema(name = "propertyType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("propertyType")
  public PropertyTypeDTO getPropertyType() {
    return propertyType;
  }

  public void setPropertyType(PropertyTypeDTO propertyType) {
    this.propertyType = propertyType;
  }

  public ListingPrivateDetailsDTO roomType(RoomTypeDTO roomType) {
    this.roomType = roomType;
    return this;
  }

  /**
   * Get roomType
   * @return roomType
  */
  @Valid 
  @Schema(name = "roomType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roomType")
  public RoomTypeDTO getRoomType() {
    return roomType;
  }

  public void setRoomType(RoomTypeDTO roomType) {
    this.roomType = roomType;
  }

  public ListingPrivateDetailsDTO address(AddressDTO address) {
    this.address = address;
    return this;
  }

  /**
   * Get address
   * @return address
  */
  @Valid 
  @Schema(name = "address", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("address")
  public AddressDTO getAddress() {
    return address;
  }

  public void setAddress(AddressDTO address) {
    this.address = address;
  }

  public ListingPrivateDetailsDTO location(Object location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
  */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public Object getLocation() {
    return location;
  }

  public void setLocation(Object location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListingPrivateDetailsDTO listingPrivateDetailsDTO = (ListingPrivateDetailsDTO) o;
    return Objects.equals(this.id, listingPrivateDetailsDTO.id) &&
        Objects.equals(this.title, listingPrivateDetailsDTO.title) &&
        Objects.equals(this.status, listingPrivateDetailsDTO.status) &&
        Objects.equals(this.description, listingPrivateDetailsDTO.description) &&
        Objects.equals(this.guestCapacity, listingPrivateDetailsDTO.guestCapacity) &&
        Objects.equals(this.propertyType, listingPrivateDetailsDTO.propertyType) &&
        Objects.equals(this.roomType, listingPrivateDetailsDTO.roomType) &&
        Objects.equals(this.address, listingPrivateDetailsDTO.address) &&
        Objects.equals(this.location, listingPrivateDetailsDTO.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, status, description, guestCapacity, propertyType, roomType, address, location);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListingPrivateDetailsDTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    guestCapacity: ").append(toIndentedString(guestCapacity)).append("\n");
    sb.append("    propertyType: ").append(toIndentedString(propertyType)).append("\n");
    sb.append("    roomType: ").append(toIndentedString(roomType)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
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

