package com.stockapp.newsservice.repository;

import com.stockapp.newsservice.domain.CompanyRef;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the CompanyRef entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRefRepository extends ReactiveMongoRepository<CompanyRef, String> {
    Flux<CompanyRef> findAllBy(Pageable pageable);
}
