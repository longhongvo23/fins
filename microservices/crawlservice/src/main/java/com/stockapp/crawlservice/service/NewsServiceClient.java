package com.stockapp.crawlservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * REST client for NewsService internal APIs
 * Sends crawled news data to NewsService for persistence
 */
@Service
public class NewsServiceClient {

    private static final Logger log = LoggerFactory.getLogger(NewsServiceClient.class);

    private final WebClient webClient;

    public NewsServiceClient(
            WebClient.Builder webClientBuilder,
            @Value("${application.news-service.url:http://newsservice:8082}") String newsServiceUrl) {
        this.webClient = webClientBuilder
                .baseUrl(newsServiceUrl)
                .build();
    }

    /**
     * Save news articles to NewsService
     * POST /api/internal/news/bulk
     */
    public Mono<Void> saveNews(Object newsData) {
        log.info("Sending news articles to NewsService");

        return webClient.post()
                .uri("/api/internal/news/bulk")
                .bodyValue(newsData)
                .retrieve()
                .bodyToMono(BulkNewsResponse.class)
                .flatMap(response -> {
                    if (response != null) {
                        log.info("Successfully saved {} news articles to NewsService. Message: {}",
                                response.processedCount(), response.message());
                    } else {
                        log.warn("Received null response from NewsService");
                    }
                    return Mono.empty();
                })
                .then()
                .doOnError(error -> log.error("Failed to save news articles to NewsService: {}", error.getMessage()));
    }

    // Response DTO
    private record BulkNewsResponse(Integer processedCount, String message) {
    }
}
