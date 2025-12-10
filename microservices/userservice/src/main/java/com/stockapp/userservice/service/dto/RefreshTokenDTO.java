package com.stockapp.userservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.userservice.domain.RefreshToken} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RefreshTokenDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    @Size(max = 500)
    private String token;

    @NotNull(message = "must not be null")
    private Instant expiryDate;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant usedDate;

    @Size(max = 45)
    private String ipAddress;

    @Size(max = 1024)
    private String userAgent;

    @Size(max = 100)
    private String deviceId;

    @NotNull(message = "must not be null")
    private Boolean revoked;

    private Instant revokedDate;

    @Size(max = 500)
    private String revokedReason;

    @Size(max = 500)
    private String replacedByToken;

    private AppUserDTO user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getUsedDate() {
        return usedDate;
    }

    public void setUsedDate(Instant usedDate) {
        this.usedDate = usedDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public Instant getRevokedDate() {
        return revokedDate;
    }

    public void setRevokedDate(Instant revokedDate) {
        this.revokedDate = revokedDate;
    }

    public String getRevokedReason() {
        return revokedReason;
    }

    public void setRevokedReason(String revokedReason) {
        this.revokedReason = revokedReason;
    }

    public String getReplacedByToken() {
        return replacedByToken;
    }

    public void setReplacedByToken(String replacedByToken) {
        this.replacedByToken = replacedByToken;
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
        if (!(o instanceof RefreshTokenDTO)) {
            return false;
        }

        RefreshTokenDTO refreshTokenDTO = (RefreshTokenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, refreshTokenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RefreshTokenDTO{" +
            "id='" + getId() + "'" +
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
            ", user=" + getUser() +
            "}";
    }
}
