package com.stockapp.userservice.repository;

import com.stockapp.userservice.domain.AppUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the AppUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppUserRepository extends ReactiveMongoRepository<AppUser, String> {
    Flux<AppUser> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<AppUser> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<AppUser> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<AppUser> findOneWithEagerRelationships(String id);
}
