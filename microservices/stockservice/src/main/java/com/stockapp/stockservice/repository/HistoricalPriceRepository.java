package com.stockapp.stockservice.repository;

import com.stockapp.stockservice.domain.HistoricalPrice;
import org.springframework.data.domain.Pageable;
import java.time.Instant;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the HistoricalPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoricalPriceRepository extends ReactiveMongoRepository<HistoricalPrice, String> {
    Flux<HistoricalPrice> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<HistoricalPrice> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<HistoricalPrice> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<HistoricalPrice> findOneWithEagerRelationships(String id);

    // Custom methods for symbol filtering
    Flux<HistoricalPrice> findBySymbol(String symbol, Pageable pageable);

    Mono<Long> countBySymbol(String symbol);

    /**
     * Check if historical price exists for symbol and datetime
     */
    Mono<Boolean> existsBySymbolAndDatetime(String symbol, Instant datetime);

    /**
     * Find latest datetime for a symbol (for incremental backfill)
     * Sort by datetime descending and take first result
     */
    Flux<HistoricalPrice> findBySymbolOrderByDatetimeDesc(String symbol);

    /**
     * Find by symbol and datetime
     */
    Mono<HistoricalPrice> findBySymbolAndDatetime(String symbol, Instant datetime);

    /**
     * Find by symbol and datetime range
     * Used for chart data and historical queries
     */
    Flux<HistoricalPrice> findBySymbolAndDatetimeBetween(String symbol, Instant fromDate, Instant toDate,
            Pageable pageable);
}
