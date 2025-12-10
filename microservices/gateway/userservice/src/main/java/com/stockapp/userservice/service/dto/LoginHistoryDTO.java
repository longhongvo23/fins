package com.stockapp.userservice.service.dto;

import com.stockapp.userservice.domain.enumeration.DeviceType;
import com.stockapp.userservice.domain.enumeration.LoginMethod;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.userservice.domain.LoginHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoginHistoryDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private Instant loginTime;

    @NotNull(message = "must not be null")
    private LoginMethod loginMethod;

    @Size(max = 45)
    private String ipAddress;

    @Size(max = 1024)
    private String userAgent;

    private DeviceType deviceType;

    @Size(max = 200)
    private String location;

    @NotNull(message = "must not be null")
    private Boolean successful;

    @Size(max = 500)
    private String failureReason;

    private AppUserDTO user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Instant loginTime) {
        this.loginTime = loginTime;
    }

    public LoginMethod getLoginMethod() {
        return loginMethod;
    }

    public void setLoginMethod(LoginMethod loginMethod) {
        this.loginMethod = loginMethod;
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

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
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
        if (!(o instanceof LoginHistoryDTO)) {
            return false;
        }

        LoginHistoryDTO loginHistoryDTO = (LoginHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loginHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoginHistoryDTO{" +
            "id='" + getId() + "'" +
            ", loginTime='" + getLoginTime() + "'" +
            ", loginMethod='" + getLoginMethod() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", userAgent='" + getUserAgent() + "'" +
            ", deviceType='" + getDeviceType() + "'" +
            ", location='" + getLocation() + "'" +
            ", successful='" + getSuccessful() + "'" +
            ", failureReason='" + getFailureReason() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
