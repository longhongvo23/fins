package com.stockapp.crawlservice.config;

import com.stockapp.crawlservice.service.HistoricalBackfillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Auto-run historical backfill on application startup
 * Ensures StockService has data before schedulers run
 */
@Component
public class StartupDataLoader implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(StartupDataLoader.class);

    private final HistoricalBackfillService backfillService;

    public StartupDataLoader(HistoricalBackfillService backfillService) {
        this.backfillService = backfillService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("🚀 Application started - Triggering historical backfill job");

        backfillService.executeBackfill()
                .doOnSuccess(v -> log.info("✅ Historical backfill completed - StockService now has data!"))
                .doOnError(error -> log.error("❌ Historical backfill failed: {}", error.getMessage()))
                .subscribe();
    }
}
