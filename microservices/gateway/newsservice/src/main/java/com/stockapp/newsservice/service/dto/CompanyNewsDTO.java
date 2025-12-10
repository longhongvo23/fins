package com.stockapp.newsservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.newsservice.domain.CompanyNews} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompanyNewsDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String uuid;

    @NotNull(message = "must not be null")
    private String title;

    private String description;

    private String snippet;

    @NotNull(message = "must not be null")
    @Size(max = 2048)
    private String url;

    @Size(max = 2048)
    private String imageUrl;

    private String language;

    @NotNull(message = "must not be null")
    private Instant publishedAt;

    @NotNull(message = "must not be null")
    private String source;

    private String keywords;

    private BigDecimal relevanceScore;

    private CompanyRefDTO company;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public BigDecimal getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(BigDecimal relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    public CompanyRefDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyRefDTO company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyNewsDTO)) {
            return false;
        }

        CompanyNewsDTO companyNewsDTO = (CompanyNewsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companyNewsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyNewsDTO{" +
            "id='" + getId() + "'" +
            ", uuid='" + getUuid() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", snippet='" + getSnippet() + "'" +
            ", url='" + getUrl() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", language='" + getLanguage() + "'" +
            ", publishedAt='" + getPublishedAt() + "'" +
            ", source='" + getSource() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", relevanceScore=" + getRelevanceScore() +
            ", company=" + getCompany() +
            "}";
    }
}
