package com.stockapp.userservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Userservice.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String baseUrl;

    // jhipster-needle-application-properties-property

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    // jhipster-needle-application-properties-property-getter

    // jhipster-needle-application-properties-property-class
}
