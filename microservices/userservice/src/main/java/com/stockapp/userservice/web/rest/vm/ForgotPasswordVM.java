package com.stockapp.userservice.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * View Model for forgot password request
 */
@Schema(description = "Request to initiate password reset process")
public class ForgotPasswordVM implements Serializable {

    @NotNull
    @Email
    @Schema(description = "Email address to send password reset link", required = true, example = "user@example.com")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ForgotPasswordVM{" +
                "email='" + email + '\'' +
                '}';
    }
}
