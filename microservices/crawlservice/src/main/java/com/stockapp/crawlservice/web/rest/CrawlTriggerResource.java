package com.stockapp.crawlservice.web.rest;

import com.stockapp.crawlservice.service.scheduler.WeeklyCompanyProfileScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for manually triggering crawl jobs
 * Useful for testing and on-demand updates
 */
@RestController
@RequestMapping("/api/crawl")
public class CrawlTriggerResource {

    private static final Logger log = LoggerFactory.getLogger(CrawlTriggerResource.class);

    private final WeeklyCompanyProfileScheduler companyProfileScheduler;

    public CrawlTriggerResource(WeeklyCompanyProfileScheduler companyProfileScheduler) {
        this.companyProfileScheduler = companyProfileScheduler;
    }

    /**
     * POST /api/crawl/company-profiles
     * Manually trigger company profile update for all symbols
     */
    @PostMapping("/company-profiles")
    public ResponseEntity<String> triggerCompanyProfiles() {
        log.info("Manual trigger: Company profile update");
        companyProfileScheduler.updateCompanyProfiles();
        return ResponseEntity.ok("Company profile update triggered successfully");
    }
}
