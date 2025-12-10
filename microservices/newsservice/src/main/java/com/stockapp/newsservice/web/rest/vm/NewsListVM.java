package com.stockapp.newsservice.web.rest.vm;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * View Model for news list items
 * Lightweight version for list display
 */
public class NewsListVM implements Serializable {

    private String id;
    private String title;
    private String description;
    private String snippet;
    private String imageUrl;
    private String source;
    private Instant publishedAt;
    private List<String> symbols; // Related stock symbols

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    @Override
    public String toString() {
        return "NewsListVM{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", publishedAt=" + publishedAt +
                ", symbols=" + symbols +
                '}';
    }
}
