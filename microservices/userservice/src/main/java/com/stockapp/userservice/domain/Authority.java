package com.stockapp.userservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Authority.
 */
@Document(collection = "authority")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("name")
    private String name;

    @Size(max = 500)
    @Field("description")
    private String description;

    @Field("created_date")
    private Instant createdDate;

    @Size(max = 50)
    @Field("created_by")
    private String createdBy;

    @NotNull(message = "must not be null")
    @Field("is_system")
    private Boolean isSystem;

    @Field("users")
    @JsonIgnoreProperties(value = { "authorities", "profile", "notificationSetting" }, allowSetters = true)
    private Set<AppUser> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Authority id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Authority name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Authority description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Authority createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Authority createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getIsSystem() {
        return this.isSystem;
    }

    public Authority isSystem(Boolean isSystem) {
        this.setIsSystem(isSystem);
        return this;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Set<AppUser> getUsers() {
        return this.users;
    }

    public void setUsers(Set<AppUser> appUsers) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeAuthorities(this));
        }
        if (appUsers != null) {
            appUsers.forEach(i -> i.addAuthorities(this));
        }
        this.users = appUsers;
    }

    public Authority users(Set<AppUser> appUsers) {
        this.setUsers(appUsers);
        return this;
    }

    public Authority addUsers(AppUser appUser) {
        this.users.add(appUser);
        appUser.getAuthorities().add(this);
        return this;
    }

    public Authority removeUsers(AppUser appUser) {
        this.users.remove(appUser);
        appUser.getAuthorities().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authority)) {
            return false;
        }
        return getId() != null && getId().equals(((Authority) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Authority{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", isSystem='" + getIsSystem() + "'" +
            "}";
    }
}
