package com.stockapp.stockservice.repository;

import com.stockapp.stockservice.domain.Recommendation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the Recommendation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecommendationRepository extends ReactiveMongoRepository<Recommendation, String> {
    Flux<Recommendation> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<Recommendation> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<Recommendation> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<Recommendation> findOneWithEagerRelationships(String id);
}
