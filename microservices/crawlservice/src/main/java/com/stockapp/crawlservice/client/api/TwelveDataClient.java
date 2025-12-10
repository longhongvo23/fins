package com.stockapp.crawlservice.client.api;

import com.stockapp.crawlservice.client.config.ApiProperties;
import com.stockapp.crawlservice.client.dto.QuoteResponse;
import com.stockapp.crawlservice.client.dto.TimeSeriesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class TwelveDataClient {

    private static final Logger log = LoggerFactory.getLogger(TwelveDataClient.class);

    private final WebClient webClient;
    private final ApiProperties apiProperties;

    public TwelveDataClient(
            @Qualifier("twelveDataWebClient") WebClient webClient,
            ApiProperties apiProperties) {
        this.webClient = webClient;
        this.apiProperties = apiProperties;
    }

    /**
     * Fetch historical time series data for a symbol
     * 
     * @param symbol     Stock symbol (e.g., AAPL)
     * @param interval   Time interval (e.g., 1day, 1week)
     * @param startDate  Start date in YYYY-MM-DD format
     * @param endDate    End date in YYYY-MM-DD format (optional, defaults to today)
     * @param outputsize Number of data points (default 5000)
     * @return Mono of TimeSeriesResponse
     */
    public Mono<TimeSeriesResponse> getTimeSeries(
            String symbol,
            String interval,
            String startDate,
            String endDate,
            Integer outputsize) {
        log.info("Fetching time series for symbol: {}, interval: {}, from: {} to: {}",
                symbol, interval, startDate, endDate);

        return webClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/time_series")
                            .queryParam("symbol", symbol)
                            .queryParam("interval", interval)
                            .queryParam("start_date", startDate)
                            .queryParam("apikey", apiProperties.getApi().getTwelveData().getApiKey())
                            .queryParam("format", "JSON");

                    if (endDate != null && !endDate.isEmpty()) {
                        builder.queryParam("end_date", endDate);
                    }

                    if (outputsize != null) {
                        builder.queryParam("outputsize", outputsize);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(TimeSeriesResponse.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests)
                        .doBeforeRetry(retrySignal -> log.warn("Rate limited, retrying... attempt: {}",
                                retrySignal.totalRetries() + 1)))
                .doOnError(error -> log.error("Error fetching time series for {}: {}", symbol, error.getMessage()))
                .doOnSuccess(response -> log.info("Successfully fetched time series for {}: {} values",
                        symbol, response != null && response.getValues() != null ? response.getValues().size() : 0));
    }

    /**
     * Fetch latest quote for a symbol
     * 
     * @param symbol Stock symbol (e.g., AAPL)
     * @return Mono of QuoteResponse
     */
    public Mono<QuoteResponse> getQuote(String symbol) {
        log.info("Fetching quote for symbol: {}", symbol);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/quote")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiProperties.getApi().getTwelveData().getApiKey())
                        .queryParam("format", "JSON")
                        .build())
                .retrieve()
                .bodyToMono(QuoteResponse.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests)
                        .doBeforeRetry(retrySignal -> log.warn("Rate limited, retrying... attempt: {}",
                                retrySignal.totalRetries() + 1)))
                .doOnError(error -> log.error("Error fetching quote for {}: {}", symbol, error.getMessage()))
                .doOnSuccess(response -> log.info("Successfully fetched quote for {}: close={}",
                        symbol, response != null ? response.getClose() : "N/A"));
    }
}
