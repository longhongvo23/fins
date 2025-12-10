package com.stockapp.userservice.repository;

import com.stockapp.userservice.domain.Authority;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Authority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorityRepository extends ReactiveMongoRepository<Authority, String> {
    Flux<Authority> findAllBy(Pageable pageable);
}
