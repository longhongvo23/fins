package com.stockapp.userservice.repository;

import com.stockapp.userservice.domain.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the Session entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionRepository extends ReactiveMongoRepository<Session, String> {
    Flux<Session> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<Session> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<Session> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<Session> findOneWithEagerRelationships(String id);
}
