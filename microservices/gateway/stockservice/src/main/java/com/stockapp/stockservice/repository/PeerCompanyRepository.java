package com.stockapp.stockservice.repository;

import com.stockapp.stockservice.domain.PeerCompany;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the PeerCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeerCompanyRepository extends ReactiveMongoRepository<PeerCompany, String> {
    Flux<PeerCompany> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<PeerCompany> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<PeerCompany> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<PeerCompany> findOneWithEagerRelationships(String id);
}
