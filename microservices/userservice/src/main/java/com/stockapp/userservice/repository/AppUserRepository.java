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

    /**
     * Find user by login username
     */
    Mono<AppUser> findByLogin(String login);

    /**
     * Find user by email
     */
    Mono<AppUser> findByEmail(String email);

    /**
     * Find user by password reset token
     */
    Mono<AppUser> findByPasswordResetToken(String token);

    /**
     * Find user by email verification token
     */
    Mono<AppUser> findByEmailVerificationToken(String token);

    /**
     * Find user by activation key
     */
    Mono<AppUser> findByActivationKey(String key);
}
