package com.azat4dev.booking.usersms.generated.events.dto;

import java.net.URI;
import java.util.Objects;
import com.azat4dev.booking.usersms.generated.events.dto.UsersDomainEventPayloadDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UsersDomainEventDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@JsonTypeName("UsersDomainEvent")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public class UsersDomainEventDTO {

  /**
   * Gets or Sets domain
   */
  public enum DomainEnum {
    USERS("USERS");

    private String value;

    DomainEnum(String value) {
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
    public static DomainEnum fromValue(String value) {
      for (DomainEnum b : DomainEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DomainEnum domain = DomainEnum.USERS;

  private String eventId;

  private Long occurredAt;

  private UsersDomainEventPayloadDTO payload;

  public UsersDomainEventDTO() {
    super();
  }

  public UsersDomainEventDTO domain(DomainEnum domain) {
    this.domain = domain;
    return this;
  }

  /**
   * Get domain
   * @return domain
  */
  @NotNull 
  @Schema(name = "domain", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("domain")
  public DomainEnum getDomain() {
    return domain;
  }

  public void setDomain(DomainEnum domain) {
    this.domain = domain;
  }

  public UsersDomainEventDTO eventId(String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
  */
  @NotNull 
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventId")
  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public UsersDomainEventDTO occurredAt(Long occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
  */
  @NotNull 
  @Schema(name = "occurredAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("occurredAt")
  public Long getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(Long occurredAt) {
    this.occurredAt = occurredAt;
  }

  public UsersDomainEventDTO payload(UsersDomainEventPayloadDTO payload) {
    this.payload = payload;
    return this;
  }

  /**
   * Get payload
   * @return payload
  */
  @NotNull @Valid 
  @Schema(name = "payload", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("payload")
  public UsersDomainEventPayloadDTO getPayload() {
    return payload;
  }

  public void setPayload(UsersDomainEventPayloadDTO payload) {
    this.payload = payload;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UsersDomainEventDTO usersDomainEvent = (UsersDomainEventDTO) o;
    return Objects.equals(this.domain, usersDomainEvent.domain) &&
        Objects.equals(this.eventId, usersDomainEvent.eventId) &&
        Objects.equals(this.occurredAt, usersDomainEvent.occurredAt) &&
        Objects.equals(this.payload, usersDomainEvent.payload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(domain, eventId, occurredAt, payload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UsersDomainEventDTO {\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
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

