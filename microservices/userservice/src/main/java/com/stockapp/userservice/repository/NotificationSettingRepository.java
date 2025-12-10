package com.stockapp.userservice.repository;

import com.stockapp.userservice.domain.NotificationSetting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the NotificationSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationSettingRepository extends ReactiveMongoRepository<NotificationSetting, String> {
    Flux<NotificationSetting> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<NotificationSetting> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<NotificationSetting> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<NotificationSetting> findOneWithEagerRelationships(String id);
}
