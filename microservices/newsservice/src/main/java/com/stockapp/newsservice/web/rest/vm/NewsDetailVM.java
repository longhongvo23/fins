package com.stockapp.newsservice.web.rest.vm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * View Model for news article detail
 * Complete version with all fields
 */
public class NewsDetailVM implements Serializable {

    private String id;
    private String title;
    private String description;
    private String snippet;
    private String url;
    private String imageUrl;
    private String language;
    private String source;
    private String keywords;
    private Instant publishedAt;
    private BigDecimal relevanceScore;
    private List<RelatedCompany> relatedCompanies;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public BigDecimal getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(BigDecimal relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    public List<RelatedCompany> getRelatedCompanies() {
        return relatedCompanies;
    }

    public void setRelatedCompanies(List<RelatedCompany> relatedCompanies) {
        this.relatedCompanies = relatedCompanies;
    }

    /**
     * Nested class for related company info
     */
    public static class RelatedCompany implements Serializable {
        private String symbol;
        private String name;
        private String exchange;

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

        @Override
        public String toString() {
            return "RelatedCompany{" +
                    "symbol='" + symbol + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsDetailVM{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", publishedAt=" + publishedAt +
                ", relatedCompanies=" + relatedCompanies +
                '}';
    }
}
