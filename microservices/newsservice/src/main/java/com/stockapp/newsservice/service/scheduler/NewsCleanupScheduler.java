package com.stockapp.newsservice.service.scheduler;

import com.stockapp.newsservice.service.NewsIngestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Scheduled task to cleanup old news articles
 * Runs daily at midnight to remove news older than 30 days
 */
@Service
public class NewsCleanupScheduler {

    private static final Logger log = LoggerFactory.getLogger(NewsCleanupScheduler.class);
    private static final int DAYS_TO_KEEP = 30;

    private final NewsIngestionService newsIngestionService;

    public NewsCleanupScheduler(NewsIngestionService newsIngestionService) {
        this.newsIngestionService = newsIngestionService;
    }

    /**
     * Scheduled job: Daily cleanup at midnight
     * Cron: 0 0 0 * * ?
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupOldNews() {
        log.info("Starting daily news cleanup job");

        newsIngestionService.deleteOldNews(DAYS_TO_KEEP)
                .doOnSuccess(count -> {
                    if (count > 0) {
                        log.info("Successfully deleted {} old news articles", count);
                    } else {
                        log.info("No old news articles to delete");
                    }
                })
                .doOnError(error -> log.error("Error during news cleanup: {}", error.getMessage()))
                .subscribe();
    }
}
