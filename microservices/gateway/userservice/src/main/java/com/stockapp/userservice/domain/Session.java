package com.stockapp.userservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stockapp.userservice.domain.enumeration.DeviceType;
import com.stockapp.userservice.domain.enumeration.SessionStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Session.
 */
@Document(collection = "session")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Field("session_id")
    private String sessionId;

    @NotNull(message = "must not be null")
    @Size(max = 500)
    @Field("token")
    private String token;

    @Size(max = 500)
    @Field("refresh_token")
    private String refreshToken;

    @Size(max = 45)
    @Field("ip_address")
    private String ipAddress;

    @Size(max = 1024)
    @Field("user_agent")
    private String userAgent;

    @NotNull(message = "must not be null")
    @Field("login_time")
    private Instant loginTime;

    @NotNull(message = "must not be null")
    @Field("expiry_time")
    private Instant expiryTime;

    @Field("last_activity_time")
    private Instant lastActivityTime;

    @Size(max = 100)
    @Field("device_id")
    private String deviceId;

    @Size(max = 200)
    @Field("device_name")
    private String deviceName;

    @Field("device_type")
    private DeviceType deviceType;

    @Size(max = 50)
    @Field("os_name")
    private String osName;

    @Size(max = 50)
    @Field("os_version")
    private String osVersion;

    @Size(max = 50)
    @Field("browser_name")
    private String browserName;

    @Size(max = 50)
    @Field("browser_version")
    private String browserVersion;

    @NotNull(message = "must not be null")
    @Field("status")
    private SessionStatus status;

    @Field("is_trusted_device")
    private Boolean isTrustedDevice;

    @Size(max = 200)
    @Field("location")
    private String location;

    @Field("logout_time")
    private Instant logoutTime;

    @Field("revoked_at")
    private Instant revokedAt;

    @Size(max = 500)
    @Field("revoked_reason")
    private String revokedReason;

    @Field("user")
    @JsonIgnoreProperties(value = { "authorities", "profile", "notificationSetting" }, allowSetters = true)
    private AppUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Session id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public Session sessionId(String sessionId) {
        this.setSessionId(sessionId);
        return this;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getToken() {
        return this.token;
    }

    public Session token(String token) {
        this.setToken(token);
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public Session refreshToken(String refreshToken) {
        this.setRefreshToken(refreshToken);
        return this;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public Session ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public Session userAgent(String userAgent) {
        this.setUserAgent(userAgent);
        return this;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Instant getLoginTime() {
        return this.loginTime;
    }

    public Session loginTime(Instant loginTime) {
        this.setLoginTime(loginTime);
        return this;
    }

    public void setLoginTime(Instant loginTime) {
        this.loginTime = loginTime;
    }

    public Instant getExpiryTime() {
        return this.expiryTime;
    }

    public Session expiryTime(Instant expiryTime) {
        this.setExpiryTime(expiryTime);
        return this;
    }

    public void setExpiryTime(Instant expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Instant getLastActivityTime() {
        return this.lastActivityTime;
    }

    public Session lastActivityTime(Instant lastActivityTime) {
        this.setLastActivityTime(lastActivityTime);
        return this;
    }

    public void setLastActivityTime(Instant lastActivityTime) {
        this.lastActivityTime = lastActivityTime;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public Session deviceId(String deviceId) {
        this.setDeviceId(deviceId);
        return this;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public Session deviceName(String deviceName) {
        this.setDeviceName(deviceName);
        return this;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    public Session deviceType(DeviceType deviceType) {
        this.setDeviceType(deviceType);
        return this;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getOsName() {
        return this.osName;
    }

    public Session osName(String osName) {
        this.setOsName(osName);
        return this;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public Session osVersion(String osVersion) {
        this.setOsVersion(osVersion);
        return this;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getBrowserName() {
        return this.browserName;
    }

    public Session browserName(String browserName) {
        this.setBrowserName(browserName);
        return this;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserVersion() {
        return this.browserVersion;
    }

    public Session browserVersion(String browserVersion) {
        this.setBrowserVersion(browserVersion);
        return this;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public SessionStatus getStatus() {
        return this.status;
    }

    public Session status(SessionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public Boolean getIsTrustedDevice() {
        return this.isTrustedDevice;
    }

    public Session isTrustedDevice(Boolean isTrustedDevice) {
        this.setIsTrustedDevice(isTrustedDevice);
        return this;
    }

    public void setIsTrustedDevice(Boolean isTrustedDevice) {
        this.isTrustedDevice = isTrustedDevice;
    }

    public String getLocation() {
        return this.location;
    }

    public Session location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getLogoutTime() {
        return this.logoutTime;
    }

    public Session logoutTime(Instant logoutTime) {
        this.setLogoutTime(logoutTime);
        return this;
    }

    public void setLogoutTime(Instant logoutTime) {
        this.logoutTime = logoutTime;
    }

    public Instant getRevokedAt() {
        return this.revokedAt;
    }

    public Session revokedAt(Instant revokedAt) {
        this.setRevokedAt(revokedAt);
        return this;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    public String getRevokedReason() {
        return this.revokedReason;
    }

    public Session revokedReason(String revokedReason) {
        this.setRevokedReason(revokedReason);
        return this;
    }

    public void setRevokedReason(String revokedReason) {
        this.revokedReason = revokedReason;
    }

    public AppUser getUser() {
        return this.user;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public Session user(AppUser appUser) {
        this.setUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Session)) {
            return false;
        }
        return getId() != null && getId().equals(((Session) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Session{" +
            "id=" + getId() +
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
            "}";
    }
}
