package com.stockapp.userservice.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * View Model for reset password request
 */
@Schema(description = "Request to reset password with token")
public class ResetPasswordVM implements Serializable {

    @NotNull
    @Size(min = 1, max = 100)
    @Schema(description = "Password reset token from email", required = true)
    private String token;

    @NotNull
    @Size(min = 4, max = 100)
    @Schema(description = "New password", required = true, example = "newSecurePassword123")
    private String newPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ResetPasswordVM{" +
                "token='" + token + '\'' +
                '}';
    }
}
