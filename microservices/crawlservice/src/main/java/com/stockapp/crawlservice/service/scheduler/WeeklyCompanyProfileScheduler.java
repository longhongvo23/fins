package com.stockapp.crawlservice.service.scheduler;

import com.stockapp.crawlservice.client.api.FinnhubClient;
import com.stockapp.crawlservice.client.config.ApiProperties;
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

/**
 * Weekly Company Profile Scheduler
 * Updates company profile information (name, country, logo, etc.) once per week
 * Company info changes infrequently, so weekly updates are sufficient
 */
@Service
public class WeeklyCompanyProfileScheduler {

    private static final Logger log = LoggerFactory.getLogger(WeeklyCompanyProfileScheduler.class);

    private final FinnhubClient finnhubClient;
    private final StockServiceClient stockServiceClient;
    private final ApiProperties apiProperties;
    private final CrawlJobStateRepository crawlJobStateRepository;

    public WeeklyCompanyProfileScheduler(
            FinnhubClient finnhubClient,
            StockServiceClient stockServiceClient,
            ApiProperties apiProperties,
            CrawlJobStateRepository crawlJobStateRepository) {
        this.finnhubClient = finnhubClient;
        this.stockServiceClient = stockServiceClient;
        this.apiProperties = apiProperties;
        this.crawlJobStateRepository = crawlJobStateRepository;
    }

    /**
     * Scheduled job: Weekly company profile update
     * Cron: 0 0 3 * * MON = 3:00 AM UTC every Monday
     * Runs after weekly recommendations (2 AM) to batch API calls
     */
    @Scheduled(cron = "${application.crawl.schedule.weekly-company-profile}")
    public void updateCompanyProfiles() {
        log.info("Starting weekly company profile update job");

        Flux.fromIterable(apiProperties.getStock().getSymbols())
                .flatMap(symbol -> updateJobState(symbol + "_PROFILE", JobStatus.RUNNING, null)
                        .then(finnhubClient.getCompanyProfile(symbol))
                        .flatMap(profile -> {
                            log.info("Updating company profile for {}: {}", symbol, profile.getName());
                            return stockServiceClient.saveCompanyProfile(symbol, profile);
                        })
                        .then(updateJobState(symbol + "_PROFILE", JobStatus.SUCCEEDED, null))
                        .onErrorResume(error -> {
                            log.error("Error updating profile for {}: {}", symbol, error.getMessage());
                            return updateJobState(symbol + "_PROFILE", JobStatus.FAILED, error.getMessage());
                        })
                        .delayElement(Duration.ofSeconds(1)) // Rate limiting: 1 call/second
                )
                .then()
                .doOnSuccess(v -> log.info("Weekly company profile update completed successfully"))
                .doOnError(error -> log.error("Weekly company profile update failed: {}", error.getMessage()))
                .subscribe();
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
}
