package com.stockapp.stockservice.repository;

import com.stockapp.stockservice.domain.HistoricalPrice;
import org.springframework.data.domain.Pageable;
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
}
