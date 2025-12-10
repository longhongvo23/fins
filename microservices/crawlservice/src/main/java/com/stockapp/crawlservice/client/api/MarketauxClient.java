package com.stockapp.crawlservice.client.api;

import com.stockapp.crawlservice.client.config.ApiProperties;
import com.stockapp.crawlservice.client.dto.NewsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Component
public class MarketauxClient {

    private static final Logger log = LoggerFactory.getLogger(MarketauxClient.class);

    private final WebClient webClient;
    private final ApiProperties apiProperties;

    public MarketauxClient(
            @Qualifier("marketauxWebClient") WebClient webClient,
            ApiProperties apiProperties) {
        this.webClient = webClient;
        this.apiProperties = apiProperties;
    }

    /**
     * Fetch news articles for specific symbols
     * 
     * @param symbols  List of stock symbols (e.g., AAPL,NVDA,MSFT)
     * @param limit    Maximum number of articles to fetch (default 10, max 100)
     * @param language Language filter (default "en")
     * @return Mono of NewsResponse
     */
    public Mono<NewsResponse> getNews(List<String> symbols, Integer limit, String language) {
        String symbolsParam = String.join(",", symbols);
        log.info("Fetching news for symbols: {}, limit: {}", symbolsParam, limit);

        return webClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/news/all")
                            .queryParam("symbols", symbolsParam)
                            .queryParam("filter_entities", "true")
                            .queryParam("api_token", apiProperties.getApi().getMarketaux().getApiKey());

                    if (limit != null) {
                        builder.queryParam("limit", limit);
                    }

                    if (language != null && !language.isEmpty()) {
                        builder.queryParam("language", language);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(NewsResponse.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests)
                        .doBeforeRetry(retrySignal -> log.warn("Rate limited, retrying... attempt: {}",
                                retrySignal.totalRetries() + 1)))
                .doOnError(error -> log.error("Error fetching news for {}: {}", symbolsParam, error.getMessage()))
                .doOnSuccess(response -> log.info("Successfully fetched news for {}: {} articles",
                        symbolsParam, response != null && response.getData() != null ? response.getData().size() : 0));
    }

    /**
     * Fetch news articles with date range filter
     * 
     * @param symbols         List of stock symbols
     * @param publishedAfter  ISO 8601 datetime (e.g., 2024-01-01T00:00:00)
     * @param publishedBefore ISO 8601 datetime
     * @param limit           Maximum number of articles
     * @return Mono of NewsResponse
     */
    public Mono<NewsResponse> getNewsWithDateRange(
            List<String> symbols,
            String publishedAfter,
            String publishedBefore,
            Integer limit) {
        String symbolsParam = String.join(",", symbols);
        log.info("Fetching news for symbols: {} from {} to {}", symbolsParam, publishedAfter, publishedBefore);

        return webClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/news/all")
                            .queryParam("symbols", symbolsParam)
                            .queryParam("filter_entities", "true")
                            .queryParam("api_token", apiProperties.getApi().getMarketaux().getApiKey());

                    if (publishedAfter != null && !publishedAfter.isEmpty()) {
                        builder.queryParam("published_after", publishedAfter);
                    }

                    if (publishedBefore != null && !publishedBefore.isEmpty()) {
                        builder.queryParam("published_before", publishedBefore);
                    }

                    if (limit != null) {
                        builder.queryParam("limit", limit);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(NewsResponse.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests)
                        .doBeforeRetry(retrySignal -> log.warn("Rate limited, retrying... attempt: {}",
                                retrySignal.totalRetries() + 1)))
                .doOnError(error -> log.error("Error fetching news with date range for {}: {}", symbolsParam,
                        error.getMessage()))
                .doOnSuccess(response -> log.info("Successfully fetched news for {}: {} articles",
                        symbolsParam, response != null && response.getData() != null ? response.getData().size() : 0));
    }
}
