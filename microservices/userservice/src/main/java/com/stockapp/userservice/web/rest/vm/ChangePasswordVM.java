package com.stockapp.userservice.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * View Model for change password request
 */
@Schema(description = "Request to change password for authenticated user")
public class ChangePasswordVM implements Serializable {

    @NotNull
    @Size(min = 4, max = 100)
    @Schema(description = "Current password", required = true)
    private String currentPassword;

    @NotNull
    @Size(min = 4, max = 100)
    @Schema(description = "New password", required = true)
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ChangePasswordVM{}";
    }
}
