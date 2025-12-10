package com.stockapp.stockservice.repository;

import com.stockapp.stockservice.domain.Financial;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the Financial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinancialRepository extends ReactiveMongoRepository<Financial, String> {
    Flux<Financial> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<Financial> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<Financial> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<Financial> findOneWithEagerRelationships(String id);
}
