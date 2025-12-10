package com.stockapp.newsservice.repository;

import com.stockapp.newsservice.domain.CompanyNews;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Spring Data MongoDB reactive repository for the CompanyNews entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyNewsRepository extends ReactiveMongoRepository<CompanyNews, String> {
    Flux<CompanyNews> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<CompanyNews> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<CompanyNews> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<CompanyNews> findOneWithEagerRelationships(String id);

    /**
     * Find news by UUID
     */
    Mono<CompanyNews> findByUuid(String uuid);

    /**
     * Find all news ordered by published date descending
     */
    Flux<CompanyNews> findAllByOrderByPublishedAtDesc();

    /**
     * Delete news older than specified date
     */
    Mono<Long> deleteByPublishedAtBefore(Instant cutoffDate);
}
