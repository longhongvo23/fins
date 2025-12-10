package com.stockapp.notificationservice.web.rest.vm;

import com.stockapp.notificationservice.domain.enumeration.NotificationStatus;
import com.stockapp.notificationservice.domain.enumeration.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;

/**
 * View Model for Notification
 */
@Schema(description = "Notification view model")
public class NotificationVM implements Serializable {

    @Schema(description = "Notification ID")
    private String id;

    @Schema(description = "Recipient identifier")
    private String recipient;

    @Schema(description = "Notification type")
    private NotificationType type;

    @Schema(description = "Notification status")
    private NotificationStatus status;

    @Schema(description = "Subject/title")
    private String subject;

    @Schema(description = "Content/message")
    private String content;

    @Schema(description = "Created timestamp")
    private Instant createdAt;

    @Schema(description = "Sent timestamp")
    private Instant sentAt;

    @Schema(description = "Read status")
    private Boolean read = false;

    @Schema(description = "Related stock symbol")
    private String relatedSymbol;

    @Schema(description = "Action URL")
    private String actionUrl;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getRelatedSymbol() {
        return relatedSymbol;
    }

    public void setRelatedSymbol(String relatedSymbol) {
        this.relatedSymbol = relatedSymbol;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
}
