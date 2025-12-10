package com.stockapp.stockservice.web.rest.internal;

import com.stockapp.stockservice.domain.IntradayQuote;
import com.stockapp.stockservice.repository.IntradayQuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Internal REST API for CrawlService to update daily quotes
 * NOT exposed to public API Gateway
 */
@RestController
@RequestMapping("/api/internal/quotes")
public class IntradayQuoteInternalResource {

    private static final Logger log = LoggerFactory.getLogger(IntradayQuoteInternalResource.class);

    private final IntradayQuoteRepository intradayQuoteRepository;

    public IntradayQuoteInternalResource(IntradayQuoteRepository intradayQuoteRepository) {
        this.intradayQuoteRepository = intradayQuoteRepository;
    }

    /**
     * POST /api/internal/quotes/update
     * Update daily quote from TwelveData API
     */
    @PostMapping("/update")
    public Mono<ResponseEntity<Void>> updateDailyQuote(@RequestBody QuoteUpdateRequest request) {
        log.info("Received quote update for symbol: {}", request.symbol());

        return intradayQuoteRepository.findBySymbol(request.symbol())
                .switchIfEmpty(Mono.fromSupplier(() -> {
                    IntradayQuote newQuote = new IntradayQuote();
                    newQuote.setSymbol(request.symbol());
                    return newQuote;
                }))
                .flatMap(quote -> {
                    // Update all fields from request
                    quote.setDatetime(request.datetime());
                    quote.setTimestamp(request.timestamp());
                    quote.setOpen(parseBigDecimal(request.open()));
                    quote.setHigh(parseBigDecimal(request.high()));
                    quote.setLow(parseBigDecimal(request.low()));
                    quote.setClose(parseBigDecimal(request.close()));
                    quote.setVolume(parseLong(request.volume()));
                    quote.setPreviousClose(parseBigDecimal(request.previousClose()));
                    quote.setChange(parseBigDecimal(request.change()));
                    quote.setPercentChange(parseBigDecimal(request.percentChange()));
                    quote.setAverageVolume(parseLong(request.averageVolume()));
                    quote.setIsMarketOpen(request.isMarketOpen());
                    quote.setUpdatedAt(Instant.now());

                    return intradayQuoteRepository.save(quote);
                })
                .then(Mono.just(ResponseEntity.status(HttpStatus.OK).<Void>build()))
                .doOnSuccess(v -> log.info("Successfully updated quote for {}", request.symbol()))
                .doOnError(error -> log.error("Error updating quote for {}: {}", request.symbol(), error.getMessage()));
    }

    private Instant parseInstant(String datetime) {
        try {
            return Instant.parse(datetime);
        } catch (Exception e) {
            log.warn("Failed to parse datetime: {}", datetime);
            return Instant.now();
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return value != null ? new BigDecimal(value) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private Long parseLong(String value) {
        try {
            return value != null ? Long.parseLong(value) : null;
        } catch (Exception e) {
            return null;
        }
    }

    // Request DTO
    public record QuoteUpdateRequest(
            String symbol,
            String datetime,
            Long timestamp,
            String open,
            String high,
            String low,
            String close,
            String volume,
            String previousClose,
            String change,
            String percentChange,
            String averageVolume,
            Boolean isMarketOpen) {
    }
}
