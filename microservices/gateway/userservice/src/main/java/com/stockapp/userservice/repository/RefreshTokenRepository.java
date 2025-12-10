package com.stockapp.userservice.repository;

import com.stockapp.userservice.domain.RefreshToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the RefreshToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RefreshTokenRepository extends ReactiveMongoRepository<RefreshToken, String> {
    Flux<RefreshToken> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<RefreshToken> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<RefreshToken> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<RefreshToken> findOneWithEagerRelationships(String id);
}
