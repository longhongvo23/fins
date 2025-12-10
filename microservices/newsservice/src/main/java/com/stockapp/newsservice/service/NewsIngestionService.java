package com.stockapp.newsservice.service;

import com.stockapp.newsservice.client.dto.NewsResponse;
import com.stockapp.newsservice.domain.CompanyNews;
import com.stockapp.newsservice.domain.NewsEntity;
import com.stockapp.newsservice.repository.CompanyNewsRepository;
import com.stockapp.newsservice.repository.NewsEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for ingesting news data from external crawl service
 * Processes and persists news articles with their entities
 */
@Service
public class NewsIngestionService {

    private static final Logger log = LoggerFactory.getLogger(NewsIngestionService.class);
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    private final CompanyNewsRepository companyNewsRepository;
    private final NewsEntityRepository newsEntityRepository;

    public NewsIngestionService(
            CompanyNewsRepository companyNewsRepository,
            NewsEntityRepository newsEntityRepository) {
        this.companyNewsRepository = companyNewsRepository;
        this.newsEntityRepository = newsEntityRepository;
    }

    /**
     * Process and save news data from crawl service
     * 
     * @param newsResponse News response from external API
     * @return Mono of saved count
     */
    public Mono<Integer> ingestNews(NewsResponse newsResponse) {
        if (newsResponse == null || newsResponse.getData() == null || newsResponse.getData().isEmpty()) {
            log.warn("Received empty news response, nothing to process");
            return Mono.just(0);
        }

        int totalArticles = newsResponse.getData().size();
        log.info("Starting ingestion of {} news articles from crawlservice", totalArticles);

        return Flux.fromIterable(newsResponse.getData())
                .flatMap(this::processNewsArticle)
                .collectList()
                .map(savedList -> {
                    int savedCount = savedList.size();
                    int skippedCount = totalArticles - savedCount;
                    log.info("Ingestion complete: {} saved, {} skipped (duplicates), {} total",
                            savedCount, skippedCount, totalArticles);
                    return savedCount;
                })
                .doOnError(error -> log.error("Error during news ingestion: {}", error.getMessage(), error));
    }

    /**
     * Process a single news article with its entities
     */
    private Mono<CompanyNews> processNewsArticle(NewsResponse.NewsData newsData) {
        try {
            // Check if article already exists by UUID
            return companyNewsRepository.findByUuid(newsData.getUuid())
                    .flatMap(existing -> {
                        log.debug("News article {} already exists, skipping", newsData.getUuid());
                        return Mono.just(existing);
                    })
                    .switchIfEmpty(Mono.defer(() -> saveNewArticle(newsData)));
        } catch (Exception e) {
            log.error("Error processing news article {}: {}", newsData.getUuid(), e.getMessage());
            return Mono.empty();
        }
    }

    /**
     * Save a new news article with entities
     */
    private Mono<CompanyNews> saveNewArticle(NewsResponse.NewsData newsData) {
        CompanyNews companyNews = new CompanyNews();

        companyNews.setUuid(newsData.getUuid());
        companyNews.setTitle(newsData.getTitle());
        companyNews.setDescription(newsData.getDescription());
        companyNews.setSnippet(newsData.getSnippet());
        companyNews.setUrl(newsData.getUrl());
        companyNews.setImageUrl(newsData.getImageUrl());
        companyNews.setLanguage(newsData.getLanguage());
        companyNews.setSource(newsData.getSource());
        companyNews.setKeywords(newsData.getKeywords());

        // Parse published date
        if (newsData.getPublishedAt() != null) {
            try {
                companyNews.setPublishedAt(Instant.parse(newsData.getPublishedAt()));
            } catch (Exception e) {
                log.warn("Failed to parse date {}: {}", newsData.getPublishedAt(), e.getMessage());
                companyNews.setPublishedAt(Instant.now());
            }
        } else {
            companyNews.setPublishedAt(Instant.now());
        }

        // Set relevance score
        if (newsData.getRelevance() != null) {
            companyNews.setRelevanceScore(BigDecimal.valueOf(newsData.getRelevance()));
        }

        // Create entities if present
        Set<NewsEntity> entities = new HashSet<>();
        if (newsData.getEntities() != null && !newsData.getEntities().isEmpty()) {
            entities = newsData.getEntities().stream()
                    .map(entityStr -> createNewsEntity(entityStr, newsData.getUuid()))
                    .collect(Collectors.toSet());
        }

        companyNews.setEntities(entities);

        // Save the news article
        return companyNewsRepository.save(companyNews)
                .doOnSuccess(saved -> log.debug("Saved news article: {}", saved.getUuid()))
                .doOnError(error -> log.error("Failed to save news article {}: {}",
                        newsData.getUuid(), error.getMessage()));
    }

    /**
     * Create NewsEntity from entity string (format: "SYMBOL|Name|Exchange")
     */
    private NewsEntity createNewsEntity(String entityStr, String newsUuid) {
        NewsEntity entity = new NewsEntity();
        entity.setNewsUuid(newsUuid);

        // Parse entity string (e.g., "AAPL|Apple Inc.|NASDAQ")
        String[] parts = entityStr.split("\\|");

        if (parts.length > 0) {
            entity.setSymbol(parts[0].trim());
        }
        if (parts.length > 1) {
            entity.setName(parts[1].trim());
        }
        if (parts.length > 2) {
            entity.setExchange(parts[2].trim());
        }

        return entity;
    }

    /**
     * Get news by symbol
     */
    public Flux<CompanyNews> getNewsBySymbol(String symbol, int limit) {
        return newsEntityRepository.findBySymbol(symbol)
                .flatMap(entity -> companyNewsRepository.findByUuid(entity.getNewsUuid()))
                .distinct(CompanyNews::getUuid)
                .sort((a, b) -> b.getPublishedAt().compareTo(a.getPublishedAt()))
                .take(limit);
    }

    /**
     * Get latest news across all symbols
     */
    public Flux<CompanyNews> getLatestNews(int limit) {
        return companyNewsRepository.findAllByOrderByPublishedAtDesc()
                .take(limit);
    }

    /**
     * Delete old news articles (older than specified days)
     */
    public Mono<Long> deleteOldNews(int daysToKeep) {
        Instant cutoffDate = Instant.now().minusSeconds(daysToKeep * 24L * 60L * 60L);
        log.info("Deleting news articles older than {}", cutoffDate);

        return companyNewsRepository.deleteByPublishedAtBefore(cutoffDate)
                .doOnSuccess(count -> log.info("Deleted {} old news articles", count));
    }
}
