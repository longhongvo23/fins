package com.stockapp.crawlservice.service.scheduler;

import com.stockapp.crawlservice.service.HistoricalBackfillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Daily scheduler to incrementally update historical prices
 * Runs at 6:00 AM UTC every day to fetch yesterday's data
 * Uses smart incremental backfill - only fetches from latest DB date to now
 */
@Component
@ConditionalOnProperty(name = "application.crawl.historical.enabled", havingValue = "true", matchIfMissing = true)
public class DailyHistoricalUpdateScheduler {

    private static final Logger log = LoggerFactory.getLogger(DailyHistoricalUpdateScheduler.class);

    private final HistoricalBackfillService historicalBackfillService;

    public DailyHistoricalUpdateScheduler(HistoricalBackfillService historicalBackfillService) {
        this.historicalBackfillService = historicalBackfillService;
    }

    /**
     * Run daily incremental historical update at 6:00 AM UTC
     * Fetches only new data from latest DB date to current date
     * No duplicates due to smart filtering in backfill service
     */
    @Scheduled(cron = "0 0 6 * * ?", zone = "UTC")
    public void scheduleDailyHistoricalUpdate() {
        log.info("Starting daily incremental historical update");

        historicalBackfillService.executeBackfill()
                .doOnSuccess(v -> log.info("Daily incremental historical update completed"))
                .doOnError(error -> log.error("Daily incremental historical update failed: {}", error.getMessage()))
                .subscribe();
    }
}
