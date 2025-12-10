package com.stockapp.userservice.service.dto;

import com.stockapp.userservice.domain.enumeration.DeviceType;
import com.stockapp.userservice.domain.enumeration.SessionStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.userservice.domain.Session} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    private String sessionId;

    @NotNull(message = "must not be null")
    @Size(max = 500)
    private String token;

    @Size(max = 500)
    private String refreshToken;

    @Size(max = 45)
    private String ipAddress;

    @Size(max = 1024)
    private String userAgent;

    @NotNull(message = "must not be null")
    private Instant loginTime;

    @NotNull(message = "must not be null")
    private Instant expiryTime;

    private Instant lastActivityTime;

    @Size(max = 100)
    private String deviceId;

    @Size(max = 200)
    private String deviceName;

    private DeviceType deviceType;

    @Size(max = 50)
    private String osName;

    @Size(max = 50)
    private String osVersion;

    @Size(max = 50)
    private String browserName;

    @Size(max = 50)
    private String browserVersion;

    @NotNull(message = "must not be null")
    private SessionStatus status;

    private Boolean isTrustedDevice;

    @Size(max = 200)
    private String location;

    private Instant logoutTime;

    private Instant revokedAt;

    @Size(max = 500)
    private String revokedReason;

    private AppUserDTO user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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

    public Instant getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Instant loginTime) {
        this.loginTime = loginTime;
    }

    public Instant getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Instant expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Instant getLastActivityTime() {
        return lastActivityTime;
    }

    public void setLastActivityTime(Instant lastActivityTime) {
        this.lastActivityTime = lastActivityTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public Boolean getIsTrustedDevice() {
        return isTrustedDevice;
    }

    public void setIsTrustedDevice(Boolean isTrustedDevice) {
        this.isTrustedDevice = isTrustedDevice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Instant logoutTime) {
        this.logoutTime = logoutTime;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    public String getRevokedReason() {
        return revokedReason;
    }

    public void setRevokedReason(String revokedReason) {
        this.revokedReason = revokedReason;
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
        if (!(o instanceof SessionDTO)) {
            return false;
        }

        SessionDTO sessionDTO = (SessionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sessionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionDTO{" +
            "id='" + getId() + "'" +
            ", sessionId='" + getSessionId() + "'" +
            ", token='" + getToken() + "'" +
            ", refreshToken='" + getRefreshToken() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", userAgent='" + getUserAgent() + "'" +
            ", loginTime='" + getLoginTime() + "'" +
            ", expiryTime='" + getExpiryTime() + "'" +
            ", lastActivityTime='" + getLastActivityTime() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", deviceName='" + getDeviceName() + "'" +
            ", deviceType='" + getDeviceType() + "'" +
            ", osName='" + getOsName() + "'" +
            ", osVersion='" + getOsVersion() + "'" +
            ", browserName='" + getBrowserName() + "'" +
            ", browserVersion='" + getBrowserVersion() + "'" +
            ", status='" + getStatus() + "'" +
            ", isTrustedDevice='" + getIsTrustedDevice() + "'" +
            ", location='" + getLocation() + "'" +
            ", logoutTime='" + getLogoutTime() + "'" +
            ", revokedAt='" + getRevokedAt() + "'" +
            ", revokedReason='" + getRevokedReason() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
