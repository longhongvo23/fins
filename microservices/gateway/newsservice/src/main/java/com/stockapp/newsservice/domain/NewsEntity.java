package com.stockapp.newsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A NewsEntity.
 */
@Document(collection = "news_entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NewsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("news_uuid")
    private String newsUuid;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[A-Z0-9\\.]+$")
    @Field("symbol")
    private String symbol;

    @Field("name")
    private String name;

    @Field("exchange")
    private String exchange;

    @Field("country")
    private String country;

    @Field("type")
    private String type;

    @Field("industry")
    private String industry;

    @Field("match_score")
    private BigDecimal matchScore;

    @Field("sentiment_score")
    private BigDecimal sentimentScore;

    @Field("news")
    @JsonIgnoreProperties(value = { "entities", "company" }, allowSetters = true)
    private CompanyNews news;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public NewsEntity id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewsUuid() {
        return this.newsUuid;
    }

    public NewsEntity newsUuid(String newsUuid) {
        this.setNewsUuid(newsUuid);
        return this;
    }

    public void setNewsUuid(String newsUuid) {
        this.newsUuid = newsUuid;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public NewsEntity symbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return this.name;
    }

    public NewsEntity name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange() {
        return this.exchange;
    }

    public NewsEntity exchange(String exchange) {
        this.setExchange(exchange);
        return this;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getCountry() {
        return this.country;
    }

    public NewsEntity country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return this.type;
    }

    public NewsEntity type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndustry() {
        return this.industry;
    }

    public NewsEntity industry(String industry) {
        this.setIndustry(industry);
        return this;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public BigDecimal getMatchScore() {
        return this.matchScore;
    }

    public NewsEntity matchScore(BigDecimal matchScore) {
        this.setMatchScore(matchScore);
        return this;
    }

    public void setMatchScore(BigDecimal matchScore) {
        this.matchScore = matchScore;
    }

    public BigDecimal getSentimentScore() {
        return this.sentimentScore;
    }

    public NewsEntity sentimentScore(BigDecimal sentimentScore) {
        this.setSentimentScore(sentimentScore);
        return this;
    }

    public void setSentimentScore(BigDecimal sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public CompanyNews getNews() {
        return this.news;
    }

    public void setNews(CompanyNews companyNews) {
        this.news = companyNews;
    }

    public NewsEntity news(CompanyNews companyNews) {
        this.setNews(companyNews);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NewsEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((NewsEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NewsEntity{" +
            "id=" + getId() +
            ", newsUuid='" + getNewsUuid() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", name='" + getName() + "'" +
            ", exchange='" + getExchange() + "'" +
            ", country='" + getCountry() + "'" +
            ", type='" + getType() + "'" +
            ", industry='" + getIndustry() + "'" +
            ", matchScore=" + getMatchScore() +
            ", sentimentScore=" + getSentimentScore() +
            "}";
    }
}
