package com.stockapp.userservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.userservice.domain.PasswordHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PasswordHistoryDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    private String passwordHash;

    @NotNull(message = "must not be null")
    private Instant changedDate;

    @Size(max = 50)
    private String changedBy;

    @Size(max = 500)
    private String changeReason;

    private AppUserDTO user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Instant getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(Instant changedDate) {
        this.changedDate = changedDate;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public AppUserDTO getUser() {
        return user;
    }

    public void setUser(AppUserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PasswordHistoryDTO)) {
            return false;
        }

        PasswordHistoryDTO passwordHistoryDTO = (PasswordHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, passwordHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PasswordHistoryDTO{" +
            "id='" + getId() + "'" +
            ", passwordHash='" + getPasswordHash() + "'" +
            ", changedDate='" + getChangedDate() + "'" +
            ", changedBy='" + getChangedBy() + "'" +
            ", changeReason='" + getChangeReason() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
