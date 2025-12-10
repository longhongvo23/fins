package com.stockapp.notificationservice.repository;

import com.stockapp.notificationservice.domain.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends ReactiveMongoRepository<Notification, String> {
    Flux<Notification> findAllBy(Pageable pageable);

    Flux<Notification> findByRecipient(String recipient, Pageable pageable);
}
