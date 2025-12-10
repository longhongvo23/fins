package com.stockapp.userservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PasswordHistory.
 */
@Document(collection = "password_history")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PasswordHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Field("password_hash")
    private String passwordHash;

    @NotNull(message = "must not be null")
    @Field("changed_date")
    private Instant changedDate;

    @Size(max = 50)
    @Field("changed_by")
    private String changedBy;

    @Size(max = 500)
    @Field("change_reason")
    private String changeReason;

    @Field("user")
    @JsonIgnoreProperties(value = { "authorities", "profile", "notificationSetting" }, allowSetters = true)
    private AppUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public PasswordHistory id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public PasswordHistory passwordHash(String passwordHash) {
        this.setPasswordHash(passwordHash);
        return this;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Instant getChangedDate() {
        return this.changedDate;
    }

    public PasswordHistory changedDate(Instant changedDate) {
        this.setChangedDate(changedDate);
        return this;
    }

    public void setChangedDate(Instant changedDate) {
        this.changedDate = changedDate;
    }

    public String getChangedBy() {
        return this.changedBy;
    }

    public PasswordHistory changedBy(String changedBy) {
        this.setChangedBy(changedBy);
        return this;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getChangeReason() {
        return this.changeReason;
    }

    public PasswordHistory changeReason(String changeReason) {
        this.setChangeReason(changeReason);
        return this;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public AppUser getUser() {
        return this.user;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public PasswordHistory user(AppUser appUser) {
        this.setUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PasswordHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((PasswordHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PasswordHistory{" +
            "id=" + getId() +
            ", passwordHash='" + getPasswordHash() + "'" +
            ", changedDate='" + getChangedDate() + "'" +
            ", changedBy='" + getChangedBy() + "'" +
            ", changeReason='" + getChangeReason() + "'" +
            "}";
    }
}
