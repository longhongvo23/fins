package com.stockapp.newsservice.web.rest;

import com.stockapp.newsservice.client.dto.NewsResponse;
import com.stockapp.newsservice.domain.CompanyNews;
import com.stockapp.newsservice.service.NewsIngestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for internal API endpoints
 * Used for communication between microservices
 */
@RestController
@RequestMapping("/api/internal")
public class InternalNewsResource {

    private static final Logger LOG = LoggerFactory.getLogger(InternalNewsResource.class);

    private final NewsIngestionService newsIngestionService;

    public InternalNewsResource(NewsIngestionService newsIngestionService) {
        this.newsIngestionService = newsIngestionService;
    }

    /**
     * POST /api/internal/news/bulk
     * Receive and process news data from crawl service
     *
     * @param newsRequest the news data from crawl service
     * @return response with count of processed articles
     */
    @PostMapping("/news/bulk")
    public Mono<ResponseEntity<BulkNewsResponse>> receiveBulkNews(@RequestBody BulkNewsRequest newsRequest) {
        int requestSize = newsRequest != null && newsRequest.newsResponse() != null
                && newsRequest.newsResponse().getData() != null
                        ? newsRequest.newsResponse().getData().size()
                        : 0;
        LOG.info("Received bulk news request from crawlservice with {} articles", requestSize);

        if (newsRequest == null || newsRequest.newsResponse() == null) {
            LOG.warn("Received null or empty news request");
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return newsIngestionService.ingestNews(newsRequest.newsResponse())
                .map(count -> {
                    LOG.info("Successfully processed and saved {} news articles", count);
                    return ResponseEntity.ok(new BulkNewsResponse(count, "Success"));
                })
                .onErrorResume(error -> {
                    LOG.error("Error processing bulk news: {}", error.getMessage(), error);
                    return Mono.just(ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new BulkNewsResponse(0, "Error: " + error.getMessage())));
                });
    }

    /**
     * GET /api/internal/news/symbol/{symbol}
     * Get news articles for a specific symbol
     *
     * @param symbol the stock symbol
     * @param limit  maximum number of articles to return
     * @return list of news articles
     */
    @GetMapping("/news/symbol/{symbol}")
    public Flux<CompanyNews> getNewsBySymbol(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "20") int limit) {
        LOG.debug("Request to get news for symbol: {}", symbol);
        return newsIngestionService.getNewsBySymbol(symbol, limit);
    }

    /**
     * GET /api/internal/news/latest
     * Get latest news articles
     *
     * @param limit maximum number of articles to return
     * @return list of latest news articles
     */
    @GetMapping("/news/latest")
    public Flux<CompanyNews> getLatestNews(@RequestParam(defaultValue = "50") int limit) {
        LOG.debug("Request to get latest {} news articles", limit);
        return newsIngestionService.getLatestNews(limit);
    }

    /**
     * DELETE /api/internal/news/cleanup
     * Delete old news articles
     *
     * @param daysToKeep number of days to keep
     * @return count of deleted articles
     */
    @DeleteMapping("/news/cleanup")
    public Mono<ResponseEntity<CleanupResponse>> cleanupOldNews(
            @RequestParam(defaultValue = "30") int daysToKeep) {
        LOG.info("Request to cleanup news older than {} days", daysToKeep);

        return newsIngestionService.deleteOldNews(daysToKeep)
                .map(count -> {
                    LOG.info("Deleted {} old news articles", count);
                    return ResponseEntity.ok(new CleanupResponse(count, "Success"));
                })
                .onErrorResume(error -> {
                    LOG.error("Error during cleanup: {}", error.getMessage());
                    return Mono.just(ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new CleanupResponse(0L, "Error: " + error.getMessage())));
                });
    }

    // Request/Response DTOs
    public record BulkNewsRequest(NewsResponse newsResponse) {
    }

    public record BulkNewsResponse(Integer processedCount, String message) {
    }

    public record CleanupResponse(Long deletedCount, String message) {
    }
}
