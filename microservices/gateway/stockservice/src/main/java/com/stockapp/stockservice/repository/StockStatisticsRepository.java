package com.stockapp.stockservice.repository;

import com.stockapp.stockservice.domain.StockStatistics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the StockStatistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockStatisticsRepository extends ReactiveMongoRepository<StockStatistics, String> {
    Flux<StockStatistics> findAllBy(Pageable pageable);
}
