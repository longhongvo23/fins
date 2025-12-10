package com.stockapp.userservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.userservice.domain.NotificationSetting} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationSettingDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private Boolean emailEnabled;

    @NotNull(message = "must not be null")
    private Boolean smsEnabled;

    @NotNull(message = "must not be null")
    private Boolean pushEnabled;

    @NotNull(message = "must not be null")
    private Boolean inAppEnabled;

    private AppUserDTO user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(Boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public Boolean getSmsEnabled() {
        return smsEnabled;
    }

    public void setSmsEnabled(Boolean smsEnabled) {
        this.smsEnabled = smsEnabled;
    }

    public Boolean getPushEnabled() {
        return pushEnabled;
    }

    public void setPushEnabled(Boolean pushEnabled) {
        this.pushEnabled = pushEnabled;
    }

    public Boolean getInAppEnabled() {
        return inAppEnabled;
    }

    public void setInAppEnabled(Boolean inAppEnabled) {
        this.inAppEnabled = inAppEnabled;
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
        if (!(o instanceof NotificationSettingDTO)) {
            return false;
        }

        NotificationSettingDTO notificationSettingDTO = (NotificationSettingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationSettingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationSettingDTO{" +
            "id='" + getId() + "'" +
            ", emailEnabled='" + getEmailEnabled() + "'" +
            ", smsEnabled='" + getSmsEnabled() + "'" +
            ", pushEnabled='" + getPushEnabled() + "'" +
            ", inAppEnabled='" + getInAppEnabled() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
