package com.stockapp.userservice.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A NotificationSetting.
 */
@Document(collection = "notification_setting")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("email_enabled")
    private Boolean emailEnabled;

    @NotNull(message = "must not be null")
    @Field("sms_enabled")
    private Boolean smsEnabled;

    @NotNull(message = "must not be null")
    @Field("push_enabled")
    private Boolean pushEnabled;

    @NotNull(message = "must not be null")
    @Field("in_app_enabled")
    private Boolean inAppEnabled;

    @Field("user")
    private AppUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public NotificationSetting id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEmailEnabled() {
        return this.emailEnabled;
    }

    public NotificationSetting emailEnabled(Boolean emailEnabled) {
        this.setEmailEnabled(emailEnabled);
        return this;
    }

    public void setEmailEnabled(Boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public Boolean getSmsEnabled() {
        return this.smsEnabled;
    }

    public NotificationSetting smsEnabled(Boolean smsEnabled) {
        this.setSmsEnabled(smsEnabled);
        return this;
    }

    public void setSmsEnabled(Boolean smsEnabled) {
        this.smsEnabled = smsEnabled;
    }

    public Boolean getPushEnabled() {
        return this.pushEnabled;
    }

    public NotificationSetting pushEnabled(Boolean pushEnabled) {
        this.setPushEnabled(pushEnabled);
        return this;
    }

    public void setPushEnabled(Boolean pushEnabled) {
        this.pushEnabled = pushEnabled;
    }

    public Boolean getInAppEnabled() {
        return this.inAppEnabled;
    }

    public NotificationSetting inAppEnabled(Boolean inAppEnabled) {
        this.setInAppEnabled(inAppEnabled);
        return this;
    }

    public void setInAppEnabled(Boolean inAppEnabled) {
        this.inAppEnabled = inAppEnabled;
    }

    public AppUser getUser() {
        return this.user;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public NotificationSetting user(AppUser appUser) {
        this.setUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationSetting)) {
            return false;
        }
        return getId() != null && getId().equals(((NotificationSetting) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationSetting{" +
            "id=" + getId() +
            ", emailEnabled='" + getEmailEnabled() + "'" +
            ", smsEnabled='" + getSmsEnabled() + "'" +
            ", pushEnabled='" + getPushEnabled() + "'" +
            ", inAppEnabled='" + getInAppEnabled() + "'" +
            "}";
    }
}
