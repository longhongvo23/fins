package com.stockapp.crawlservice.service.scheduler;

import com.stockapp.crawlservice.client.api.FinnhubClient;
import com.stockapp.crawlservice.client.config.ApiProperties;
import com.stockapp.crawlservice.client.dto.RecommendationResponse;
import com.stockapp.crawlservice.domain.CrawlJobState;
import com.stockapp.crawlservice.domain.enumeration.JobStatus;
import com.stockapp.crawlservice.repository.CrawlJobStateRepository;
import com.stockapp.crawlservice.service.StockServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Weekly Recommendation Scheduler
 * Runs every Monday at 2 AM UTC to fetch analyst recommendations
 */
@Service
public class WeeklyRecommendationScheduler {

    private static final Logger log = LoggerFactory.getLogger(WeeklyRecommendationScheduler.class);

    private final FinnhubClient finnhubClient;
    private final ApiProperties apiProperties;
    private final CrawlJobStateRepository crawlJobStateRepository;
    private final StockServiceClient stockServiceClient;

    public WeeklyRecommendationScheduler(
            FinnhubClient finnhubClient,
            ApiProperties apiProperties,
            CrawlJobStateRepository crawlJobStateRepository,
            StockServiceClient stockServiceClient) {
        this.finnhubClient = finnhubClient;
        this.apiProperties = apiProperties;
        this.crawlJobStateRepository = crawlJobStateRepository;
        this.stockServiceClient = stockServiceClient;
    }

    /**
     * Scheduled job: Weekly recommendations at 2 AM Monday UTC
     * Cron: 0 0 2 * * MON
     */
    @Scheduled(cron = "${application.crawl.schedule.weekly-recommendation}")
    public void fetchWeeklyRecommendations() {
        log.info("Starting weekly recommendation fetch job");

        Flux.fromIterable(apiProperties.getStock().getSymbols())
                .flatMap(symbol -> fetchRecommendationsForSymbol(symbol)
                        .delayElement(Duration.ofSeconds(2)) // Rate limiting
                )
                .then()
                .doOnSuccess(v -> log.info("Weekly recommendation fetch completed successfully"))
                .doOnError(error -> log.error("Weekly recommendation fetch failed: {}", error.getMessage()))
                .subscribe();
    }

    /**
     * Fetch recommendations for a single symbol
     */
    private Mono<Void> fetchRecommendationsForSymbol(String symbol) {
        log.info("Fetching recommendations for symbol: {}", symbol);

        return updateJobState(symbol, JobStatus.RUNNING, null)
                .then(finnhubClient.getRecommendations(symbol))
                .flatMap(recommendations -> {
                    // Send to StockService for persistence
                    return stockServiceClient.saveRecommendations(
                            symbol,
                            new RecommendationsRequest(symbol, recommendations))
                            .then(updateJobState(symbol, JobStatus.SUCCEEDED, null));
                })
                .onErrorResume(error -> {
                    log.error("Error fetching recommendations for {}: {}", symbol, error.getMessage());
                    return updateJobState(symbol, JobStatus.FAILED, error.getMessage());
                })
                .then();
    }

    /**
     * Update crawl job state
     */
    private Mono<CrawlJobState> updateJobState(String symbol, JobStatus status, String errorLog) {
        return crawlJobStateRepository.findBySymbol(symbol)
                .switchIfEmpty(Mono.fromSupplier(() -> {
                    CrawlJobState newState = new CrawlJobState();
                    newState.setSymbol(symbol);
                    return newState;
                }))
                .flatMap(state -> {
                    state.setLastSyncStatus(status);

                    if (status == JobStatus.SUCCEEDED) {
                        state.setLastSuccessfulTimestamp(Instant.now());
                        state.setErrorLog(null);
                    } else if (status == JobStatus.FAILED && errorLog != null) {
                        state.setErrorLog(errorLog);
                    }

                    return crawlJobStateRepository.save(state);
                });
    }

    // Request DTO for StockService
    private record RecommendationsRequest(
            String symbol,
            List<RecommendationResponse> recommendations) {
    }
}
