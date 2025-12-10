package com.stockapp.userservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.userservice.domain.Authority} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthorityDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String name;

    @Size(max = 500)
    private String description;

    private Instant createdDate;

    @Size(max = 50)
    private String createdBy;

    @NotNull(message = "must not be null")
    private Boolean isSystem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorityDTO)) {
            return false;
        }

        AuthorityDTO authorityDTO = (AuthorityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, authorityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorityDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", isSystem='" + getIsSystem() + "'" +
            "}";
    }
}
