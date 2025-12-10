package com.stockapp.userservice.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * View Model for simple message responses
 */
@Schema(description = "Simple message response")
public class MessageResponseVM implements Serializable {

    @Schema(description = "Response message")
    private String message;

    @Schema(description = "Success status")
    private Boolean success;

    public MessageResponseVM() {
    }

    public MessageResponseVM(String message) {
        this.message = message;
        this.success = true;
    }

    public MessageResponseVM(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "MessageResponseVM{" +
                "message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
