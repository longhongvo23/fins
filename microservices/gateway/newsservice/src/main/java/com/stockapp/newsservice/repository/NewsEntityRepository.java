package com.stockapp.newsservice.repository;

import com.stockapp.newsservice.domain.NewsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the NewsEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NewsEntityRepository extends ReactiveMongoRepository<NewsEntity, String> {
    Flux<NewsEntity> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<NewsEntity> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<NewsEntity> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<NewsEntity> findOneWithEagerRelationships(String id);
}
