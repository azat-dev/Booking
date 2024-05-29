package com.azat4dev.booking.listingsms.generated.server.model;

import java.net.URI;
import java.util.Objects;
import com.azat4dev.booking.listingsms.generated.server.model.LocationDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ListingDetails
 */
@lombok.Builder(toBuilder = true)
@lombok.RequiredArgsConstructor
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-28T23:07:12.290925+03:00[Europe/Moscow]")
public class ListingDetails {

  private UUID id;

  private UUID type;

  private String title;

  private String description;

  @Valid
  private List<URI> photos = new ArrayList<>();

  private LocationDetails location;

  private UUID hostId;

  public ListingDetails id(UUID id) {
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

  public ListingDetails type(UUID type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  */
  @NotNull @Valid 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public UUID getType() {
    return type;
  }

  public void setType(UUID type) {
    this.type = type;
  }

  public ListingDetails title(String title) {
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

  public ListingDetails description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  */
  @NotNull 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ListingDetails photos(List<URI> photos) {
    this.photos = photos;
    return this;
  }

  public ListingDetails addPhotosItem(URI photosItem) {
    if (this.photos == null) {
      this.photos = new ArrayList<>();
    }
    this.photos.add(photosItem);
    return this;
  }

  /**
   * Get photos
   * @return photos
  */
  @NotNull @Valid 
  @Schema(name = "photos", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("photos")
  public List<URI> getPhotos() {
    return photos;
  }

  public void setPhotos(List<URI> photos) {
    this.photos = photos;
  }

  public ListingDetails location(LocationDetails location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
  */
  @NotNull @Valid 
  @Schema(name = "location", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("location")
  public LocationDetails getLocation() {
    return location;
  }

  public void setLocation(LocationDetails location) {
    this.location = location;
  }

  public ListingDetails hostId(UUID hostId) {
    this.hostId = hostId;
    return this;
  }

  /**
   * Get hostId
   * @return hostId
  */
  @NotNull @Valid 
  @Schema(name = "hostId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hostId")
  public UUID getHostId() {
    return hostId;
  }

  public void setHostId(UUID hostId) {
    this.hostId = hostId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListingDetails listingDetails = (ListingDetails) o;
    return Objects.equals(this.id, listingDetails.id) &&
        Objects.equals(this.type, listingDetails.type) &&
        Objects.equals(this.title, listingDetails.title) &&
        Objects.equals(this.description, listingDetails.description) &&
        Objects.equals(this.photos, listingDetails.photos) &&
        Objects.equals(this.location, listingDetails.location) &&
        Objects.equals(this.hostId, listingDetails.hostId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, title, description, photos, location, hostId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListingDetails {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    photos: ").append(toIndentedString(photos)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    hostId: ").append(toIndentedString(hostId)).append("\n");
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

