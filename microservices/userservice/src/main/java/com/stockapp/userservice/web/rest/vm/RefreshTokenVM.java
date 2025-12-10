package com.stockapp.userservice.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * View Model for refresh token request
 */
@Schema(description = "Request to refresh access token")
public class RefreshTokenVM implements Serializable {

    @NotNull
    @Schema(description = "Refresh token", required = true)
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "RefreshTokenVM{}";
    }
}
