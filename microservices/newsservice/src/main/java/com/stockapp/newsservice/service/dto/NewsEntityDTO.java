package com.stockapp.newsservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.newsservice.domain.NewsEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NewsEntityDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String newsUuid;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[A-Z0-9\\.]+$")
    private String symbol;

    private String name;

    private String exchange;

    private String country;

    private String type;

    private String industry;

    private BigDecimal matchScore;

    private BigDecimal sentimentScore;

    private CompanyNewsDTO news;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewsUuid() {
        return newsUuid;
    }

    public void setNewsUuid(String newsUuid) {
        this.newsUuid = newsUuid;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public BigDecimal getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(BigDecimal matchScore) {
        this.matchScore = matchScore;
    }

    public BigDecimal getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(BigDecimal sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public CompanyNewsDTO getNews() {
        return news;
    }

    public void setNews(CompanyNewsDTO news) {
        this.news = news;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NewsEntityDTO)) {
            return false;
        }

        NewsEntityDTO newsEntityDTO = (NewsEntityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, newsEntityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NewsEntityDTO{" +
            "id='" + getId() + "'" +
            ", newsUuid='" + getNewsUuid() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", name='" + getName() + "'" +
            ", exchange='" + getExchange() + "'" +
            ", country='" + getCountry() + "'" +
            ", type='" + getType() + "'" +
            ", industry='" + getIndustry() + "'" +
            ", matchScore=" + getMatchScore() +
            ", sentimentScore=" + getSentimentScore() +
            ", news=" + getNews() +
            "}";
    }
}
