package com.stockapp.crawlservice.service;

import com.stockapp.crawlservice.client.dto.ProfileResponse;
import com.stockapp.crawlservice.client.dto.TimeSeriesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * REST client for StockService internal APIs
 * Sends crawled data to StockService for persistence
 */
@Service
public class StockServiceClient {

        private static final Logger log = LoggerFactory.getLogger(StockServiceClient.class);

        private final WebClient webClient;

        public StockServiceClient(
                        WebClient.Builder webClientBuilder,
                        @Value("${application.stock-service.url:http://stockservice:8083}") String stockServiceUrl) {
                this.webClient = webClientBuilder
                                .baseUrl(stockServiceUrl)
                                .build();
        }

        /**
         * Save company profile to StockService
         * POST /api/internal/companies/profile
         */
        public Mono<Void> saveCompanyProfile(String symbol, ProfileResponse profile) {
                log.info("Sending company profile for {} to StockService", symbol);

                return webClient.post()
                                .uri("/api/internal/companies/profile")
                                .bodyValue(new CompanyProfileRequest(symbol, profile))
                                .retrieve()
                                .bodyToMono(Void.class)
                                .doOnSuccess(v -> log.info("Successfully saved company profile for {}", symbol))
                                .doOnError(error -> log.error("Failed to save company profile for {}: {}", symbol,
                                                error.getMessage()));
        }

        /**
         * Save historical prices to StockService in bulk
         * POST /api/internal/historical/bulk
         */
        public Mono<Void> saveHistoricalPrices(String symbol, TimeSeriesResponse timeSeries) {
                int count = timeSeries.getValues() != null ? timeSeries.getValues().size() : 0;

                return webClient.post()
                                .uri("/api/internal/historical/bulk")
                                .bodyValue(new HistoricalPricesRequest(symbol, timeSeries))
                                .retrieve()
                                .bodyToMono(Void.class)
                                .doOnSuccess(v -> log.info("Successfully saved {} historical prices for {}", count,
                                                symbol))
                                .doOnError(
                                                error -> log.error("Failed to save {} historical prices for {}: {}",
                                                                count, symbol, error.getMessage()));
        }

        /**
         * Update daily quote in StockService
         * POST /api/internal/quotes/update
         */
        public Mono<Void> updateDailyQuote(String symbol, Object quoteData) {
                log.info("Sending daily quote update for {} to StockService", symbol);

                return webClient.post()
                                .uri("/api/internal/quotes/update")
                                .bodyValue(quoteData)
                                .retrieve()
                                .bodyToMono(Void.class)
                                .doOnSuccess(v -> log.info("Successfully updated daily quote for {}", symbol))
                                .doOnError(error -> log.error("Failed to update daily quote for {}: {}", symbol,
                                                error.getMessage()));
        }

        /**
         * Get latest historical price date for a symbol (for incremental updates)
         * GET /api/internal/historical/latest/{symbol}
         */
        public Mono<String> getLatestHistoricalDate(String symbol) {
                log.debug("Querying latest historical date for {} from StockService", symbol);

                return webClient.get()
                                .uri("/api/internal/historical/latest/{symbol}", symbol)
                                .retrieve()
                                .bodyToMono(LatestDateResponse.class)
                                .flatMap(response -> {
                                        if (response.latestDate() == null) {
                                                log.debug("No existing data for {}, will use default startDate",
                                                                symbol);
                                                return Mono.empty();
                                        }
                                        return Mono.just(response.latestDate());
                                })
                                .doOnSuccess(date -> log.debug("Latest date for {}: {}", symbol,
                                                date != null ? date : "none"))
                                .doOnError(error -> log.error("Failed to get latest date for {}: {}", symbol,
                                                error.getMessage()));
        }

        /**
         * Save recommendations to StockService
         * POST /api/internal/recommendations/bulk
         */
        public Mono<Void> saveRecommendations(String symbol, Object recommendations) {
                log.info("Sending recommendations for {} to StockService", symbol);

                return webClient.post()
                                .uri("/api/internal/recommendations/bulk")
                                .bodyValue(recommendations)
                                .retrieve()
                                .bodyToMono(Void.class)
                                .doOnSuccess(v -> log.info("Successfully saved recommendations for {}", symbol))
                                .doOnError(error -> log.error("Failed to save recommendations for {}: {}", symbol,
                                                error.getMessage()));
        }

        // Request DTOs
        private record CompanyProfileRequest(String symbol, ProfileResponse profile) {
        }

        private record HistoricalPricesRequest(String symbol, TimeSeriesResponse timeSeries) {
        }

        // Response DTOs
        private record LatestDateResponse(String symbol, String latestDate) {
        }
}
