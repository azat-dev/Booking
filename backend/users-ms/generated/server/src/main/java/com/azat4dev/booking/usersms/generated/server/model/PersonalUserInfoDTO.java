package com.azat4dev.booking.usersms.generated.server.model;

import java.net.URI;
import java.util.Objects;
import com.azat4dev.booking.usersms.generated.server.model.EmailVerificationStatusDTO;
import com.azat4dev.booking.usersms.generated.server.model.FullNameDTO;
import com.azat4dev.booking.usersms.generated.server.model.PhotoPathDTO;
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
 * PersonalUserInfoDTO
 */
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-30T01:15:09.334912+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public class PersonalUserInfoDTO {

  private UUID id;

  private FullNameDTO fullName;

  private String email;

  private PhotoPathDTO photo;

  private EmailVerificationStatusDTO emailVerificationStatus;

  public PersonalUserInfoDTO() {
    super();
  }

  public PersonalUserInfoDTO id(UUID id) {
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

  public PersonalUserInfoDTO fullName(FullNameDTO fullName) {
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

  public PersonalUserInfoDTO email(String email) {
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

  public PersonalUserInfoDTO photo(PhotoPathDTO photo) {
    this.photo = photo;
    return this;
  }

  /**
   * Get photo
   * @return photo
  */
  @Valid 
  @Schema(name = "photo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("photo")
  public PhotoPathDTO getPhoto() {
    return photo;
  }

  public void setPhoto(PhotoPathDTO photo) {
    this.photo = photo;
  }

  public PersonalUserInfoDTO emailVerificationStatus(EmailVerificationStatusDTO emailVerificationStatus) {
    this.emailVerificationStatus = emailVerificationStatus;
    return this;
  }

  /**
   * Get emailVerificationStatus
   * @return emailVerificationStatus
  */
  @NotNull @Valid 
  @Schema(name = "emailVerificationStatus", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("emailVerificationStatus")
  public EmailVerificationStatusDTO getEmailVerificationStatus() {
    return emailVerificationStatus;
  }

  public void setEmailVerificationStatus(EmailVerificationStatusDTO emailVerificationStatus) {
    this.emailVerificationStatus = emailVerificationStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonalUserInfoDTO personalUserInfoDTO = (PersonalUserInfoDTO) o;
    return Objects.equals(this.id, personalUserInfoDTO.id) &&
        Objects.equals(this.fullName, personalUserInfoDTO.fullName) &&
        Objects.equals(this.email, personalUserInfoDTO.email) &&
        Objects.equals(this.photo, personalUserInfoDTO.photo) &&
        Objects.equals(this.emailVerificationStatus, personalUserInfoDTO.emailVerificationStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, email, photo, emailVerificationStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersonalUserInfoDTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    photo: ").append(toIndentedString(photo)).append("\n");
    sb.append("    emailVerificationStatus: ").append(toIndentedString(emailVerificationStatus)).append("\n");
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

