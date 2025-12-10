package com.stockapp.userservice.repository;

import com.stockapp.userservice.domain.LoginHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the LoginHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoginHistoryRepository extends ReactiveMongoRepository<LoginHistory, String> {
    Flux<LoginHistory> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<LoginHistory> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<LoginHistory> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<LoginHistory> findOneWithEagerRelationships(String id);
}
