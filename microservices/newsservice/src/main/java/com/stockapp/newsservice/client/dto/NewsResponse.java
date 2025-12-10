package com.stockapp.newsservice.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO for receiving news data from external crawl service
 */
public class NewsResponse {

    private Meta meta;
    private List<NewsData> data;

    public static class Meta {
        private Integer found;
        private Integer returned;
        private Integer limit;
        private Integer page;

        public Integer getFound() {
            return found;
        }

        public void setFound(Integer found) {
            this.found = found;
        }

        public Integer getReturned() {
            return returned;
        }

        public void setReturned(Integer returned) {
            this.returned = returned;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }
    }

    public static class NewsData {
        private String uuid;
        private String title;
        private String description;
        private String keywords;
        private String snippet;
        private String url;

        @JsonProperty("image_url")
        private String imageUrl;

        private String language;

        @JsonProperty("published_at")
        private String publishedAt;

        private String source;
        private List<String> categories;
        private Double relevance;
        private List<String> entities;

        @JsonProperty("similar")
        private List<Object> similar;

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

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
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

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public List<String> getCategories() {
            return categories;
        }

        public void setCategories(List<String> categories) {
            this.categories = categories;
        }

        public Double getRelevance() {
            return relevance;
        }

        public void setRelevance(Double relevance) {
            this.relevance = relevance;
        }

        public List<String> getEntities() {
            return entities;
        }

        public void setEntities(List<String> entities) {
            this.entities = entities;
        }

        public List<Object> getSimilar() {
            return similar;
        }

        public void setSimilar(List<Object> similar) {
            this.similar = similar;
        }
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<NewsData> getData() {
        return data;
    }

    public void setData(List<NewsData> data) {
        this.data = data;
    }
}
