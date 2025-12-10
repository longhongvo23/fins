package com.stockapp.userservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A RefreshToken.
 */
@Document(collection = "refresh_token")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RefreshToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Size(max = 500)
    @Field("token")
    private String token;

    @NotNull(message = "must not be null")
    @Field("expiry_date")
    private Instant expiryDate;

    @NotNull(message = "must not be null")
    @Field("created_date")
    private Instant createdDate;

    @Field("used_date")
    private Instant usedDate;

    @Size(max = 45)
    @Field("ip_address")
    private String ipAddress;

    @Size(max = 1024)
    @Field("user_agent")
    private String userAgent;

    @Size(max = 100)
    @Field("device_id")
    private String deviceId;

    @NotNull(message = "must not be null")
    @Field("revoked")
    private Boolean revoked;

    @Field("revoked_date")
    private Instant revokedDate;

    @Size(max = 500)
    @Field("revoked_reason")
    private String revokedReason;

    @Size(max = 500)
    @Field("replaced_by_token")
    private String replacedByToken;

    @Field("user")
    @JsonIgnoreProperties(value = { "authorities", "profile", "notificationSetting" }, allowSetters = true)
    private AppUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public RefreshToken id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public RefreshToken token(String token) {
        this.setToken(token);
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return this.expiryDate;
    }

    public RefreshToken expiryDate(Instant expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public RefreshToken createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getUsedDate() {
        return this.usedDate;
    }

    public RefreshToken usedDate(Instant usedDate) {
        this.setUsedDate(usedDate);
        return this;
    }

    public void setUsedDate(Instant usedDate) {
        this.usedDate = usedDate;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public RefreshToken ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public RefreshToken userAgent(String userAgent) {
        this.setUserAgent(userAgent);
        return this;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public RefreshToken deviceId(String deviceId) {
        this.setDeviceId(deviceId);
        return this;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Boolean getRevoked() {
        return this.revoked;
    }

    public RefreshToken revoked(Boolean revoked) {
        this.setRevoked(revoked);
        return this;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public Instant getRevokedDate() {
        return this.revokedDate;
    }

    public RefreshToken revokedDate(Instant revokedDate) {
        this.setRevokedDate(revokedDate);
        return this;
    }

    public void setRevokedDate(Instant revokedDate) {
        this.revokedDate = revokedDate;
    }

    public String getRevokedReason() {
        return this.revokedReason;
    }

    public RefreshToken revokedReason(String revokedReason) {
        this.setRevokedReason(revokedReason);
        return this;
    }

    public void setRevokedReason(String revokedReason) {
        this.revokedReason = revokedReason;
    }

    public String getReplacedByToken() {
        return this.replacedByToken;
    }

    public RefreshToken replacedByToken(String replacedByToken) {
        this.setReplacedByToken(replacedByToken);
        return this;
    }

    public void setReplacedByToken(String replacedByToken) {
        this.replacedByToken = replacedByToken;
    }

    public AppUser getUser() {
        return this.user;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public RefreshToken user(AppUser appUser) {
        this.setUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RefreshToken)) {
            return false;
        }
        return getId() != null && getId().equals(((RefreshToken) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RefreshToken{" +
            "id=" + getId() +
            ", token='" + getToken() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", usedDate='" + getUsedDate() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", userAgent='" + getUserAgent() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", revoked='" + getRevoked() + "'" +
            ", revokedDate='" + getRevokedDate() + "'" +
            ", revokedReason='" + getRevokedReason() + "'" +
            ", replacedByToken='" + getReplacedByToken() + "'" +
            "}";
    }
}
