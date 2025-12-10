package com.stockapp.stockservice.repository;

import com.stockapp.stockservice.domain.IntradayQuote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the IntradayQuote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntradayQuoteRepository extends ReactiveMongoRepository<IntradayQuote, String> {
    Flux<IntradayQuote> findAllBy(Pageable pageable);
}
