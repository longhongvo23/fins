package com.stockapp.newsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A CompanyNews.
 */
@Document(collection = "company_news")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompanyNews implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("uuid")
    private String uuid;

    @NotNull(message = "must not be null")
    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("snippet")
    private String snippet;

    @NotNull(message = "must not be null")
    @Size(max = 2048)
    @Field("url")
    private String url;

    @Size(max = 2048)
    @Field("image_url")
    private String imageUrl;

    @Field("language")
    private String language;

    @NotNull(message = "must not be null")
    @Field("published_at")
    private Instant publishedAt;

    @NotNull(message = "must not be null")
    @Field("source")
    private String source;

    @Field("keywords")
    private String keywords;

    @Field("relevance_score")
    private BigDecimal relevanceScore;

    @Field("entities")
    @JsonIgnoreProperties(value = { "news" }, allowSetters = true)
    private Set<NewsEntity> entities = new HashSet<>();

    @Field("company")
    @JsonIgnoreProperties(value = { "companyNews" }, allowSetters = true)
    private CompanyRef company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public CompanyNews id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public CompanyNews uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return this.title;
    }

    public CompanyNews title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public CompanyNews description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSnippet() {
        return this.snippet;
    }

    public CompanyNews snippet(String snippet) {
        this.setSnippet(snippet);
        return this;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getUrl() {
        return this.url;
    }

    public CompanyNews url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public CompanyNews imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLanguage() {
        return this.language;
    }

    public CompanyNews language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Instant getPublishedAt() {
        return this.publishedAt;
    }

    public CompanyNews publishedAt(Instant publishedAt) {
        this.setPublishedAt(publishedAt);
        return this;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return this.source;
    }

    public CompanyNews source(String source) {
        this.setSource(source);
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public CompanyNews keywords(String keywords) {
        this.setKeywords(keywords);
        return this;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public BigDecimal getRelevanceScore() {
        return this.relevanceScore;
    }

    public CompanyNews relevanceScore(BigDecimal relevanceScore) {
        this.setRelevanceScore(relevanceScore);
        return this;
    }

    public void setRelevanceScore(BigDecimal relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    public Set<NewsEntity> getEntities() {
        return this.entities;
    }

    public void setEntities(Set<NewsEntity> newsEntities) {
        if (this.entities != null) {
            this.entities.forEach(i -> i.setNews(null));
        }
        if (newsEntities != null) {
            newsEntities.forEach(i -> i.setNews(this));
        }
        this.entities = newsEntities;
    }

    public CompanyNews entities(Set<NewsEntity> newsEntities) {
        this.setEntities(newsEntities);
        return this;
    }

    public CompanyNews addEntities(NewsEntity newsEntity) {
        this.entities.add(newsEntity);
        newsEntity.setNews(this);
        return this;
    }

    public CompanyNews removeEntities(NewsEntity newsEntity) {
        this.entities.remove(newsEntity);
        newsEntity.setNews(null);
        return this;
    }

    public CompanyRef getCompany() {
        return this.company;
    }

    public void setCompany(CompanyRef companyRef) {
        this.company = companyRef;
    }

    public CompanyNews company(CompanyRef companyRef) {
        this.setCompany(companyRef);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyNews)) {
            return false;
        }
        return getId() != null && getId().equals(((CompanyNews) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyNews{" +
            "id=" + getId() +
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
            "}";
    }
}
