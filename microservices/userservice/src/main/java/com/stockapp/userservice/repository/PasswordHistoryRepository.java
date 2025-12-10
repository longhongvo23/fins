package com.stockapp.userservice.repository;

import com.stockapp.userservice.domain.PasswordHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the PasswordHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PasswordHistoryRepository extends ReactiveMongoRepository<PasswordHistory, String> {
    Flux<PasswordHistory> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<PasswordHistory> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<PasswordHistory> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<PasswordHistory> findOneWithEagerRelationships(String id);
}
