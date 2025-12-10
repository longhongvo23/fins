package com.stockapp.crawlservice.client.api;

import com.stockapp.crawlservice.client.config.ApiProperties;
import com.stockapp.crawlservice.client.dto.ProfileResponse;
import com.stockapp.crawlservice.client.dto.RecommendationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Component
public class FinnhubClient {

    private static final Logger log = LoggerFactory.getLogger(FinnhubClient.class);

    private final WebClient webClient;
    private final ApiProperties apiProperties;

    public FinnhubClient(
            @Qualifier("finnhubWebClient") WebClient webClient,
            ApiProperties apiProperties) {
        this.webClient = webClient;
        this.apiProperties = apiProperties;
    }

    /**
     * Fetch company profile for a symbol
     * 
     * @param symbol Stock symbol (e.g., AAPL)
     * @return Mono of ProfileResponse
     */
    public Mono<ProfileResponse> getCompanyProfile(String symbol) {
        log.info("Fetching company profile for symbol: {}", symbol);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stock/profile2")
                        .queryParam("symbol", symbol)
                        .queryParam("token", apiProperties.getApi().getFinnhub().getApiKey())
                        .build())
                .retrieve()
                .bodyToMono(ProfileResponse.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests)
                        .doBeforeRetry(retrySignal -> log.warn("Rate limited, retrying... attempt: {}",
                                retrySignal.totalRetries() + 1)))
                .doOnError(error -> log.error("Error fetching profile for {}: {}", symbol, error.getMessage()))
                .doOnSuccess(response -> log.info("Successfully fetched profile for {}: name={}",
                        symbol, response != null ? response.getName() : "N/A"));
    }

    /**
     * Fetch analyst recommendations for a symbol
     * 
     * @param symbol Stock symbol (e.g., AAPL)
     * @return Mono of List of RecommendationResponse
     */
    public Mono<List<RecommendationResponse>> getRecommendations(String symbol) {
        log.info("Fetching recommendations for symbol: {}", symbol);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stock/recommendation")
                        .queryParam("symbol", symbol)
                        .queryParam("token", apiProperties.getApi().getFinnhub().getApiKey())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<RecommendationResponse>>() {
                })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests)
                        .doBeforeRetry(retrySignal -> log.warn("Rate limited, retrying... attempt: {}",
                                retrySignal.totalRetries() + 1)))
                .doOnError(error -> log.error("Error fetching recommendations for {}: {}", symbol, error.getMessage()))
                .doOnSuccess(response -> log.info("Successfully fetched recommendations for {}: {} records",
                        symbol, response != null ? response.size() : 0));
    }
}
