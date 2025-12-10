package com.stockapp.stockservice.config;

import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.TimeSeriesGranularity;
import com.mongodb.client.model.TimeSeriesOptions;
import com.stockapp.stockservice.domain.HistoricalPrice;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import reactor.core.publisher.Mono;

/**
 * Configure MongoDB time-series collection for historical prices.
 */
@Configuration
public class TimeSeriesConfiguration {

        private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesConfiguration.class);

        private static final String TIME_FIELD = "datetime";
        private static final String META_FIELD = "symbol";

        private final ReactiveMongoTemplate mongoTemplate;

        public TimeSeriesConfiguration(ReactiveMongoTemplate mongoTemplate) {
                this.mongoTemplate = mongoTemplate;
        }

        @PostConstruct
        public void ensureHistoricalPriceTimeSeries() {
                var options = new CreateCollectionOptions()
                                .timeSeriesOptions(new TimeSeriesOptions(TIME_FIELD)
                                                .metaField(META_FIELD)
                                                .granularity(TimeSeriesGranularity.HOURS));

                Mono<Void> ensureIndexes = mongoTemplate
                                .indexOps(HistoricalPrice.COLLECTION)
                                .ensureIndex(new Index().on(META_FIELD, Sort.Direction.ASC))
                                .then(mongoTemplate.indexOps(HistoricalPrice.COLLECTION)
                                                .ensureIndex(new Index().on("interval", Sort.Direction.ASC)))
                                .then();

                mongoTemplate.collectionExists(HistoricalPrice.COLLECTION)
                                .flatMap(exists -> {
                                        if (exists) {
                                                LOG.info("Mongo collection {} already exists; ensuring indexes",
                                                                HistoricalPrice.COLLECTION);
                                                return ensureIndexes;
                                        }
                                        LOG.info("Creating Mongo time-series collection {} (timeField={}, metaField={})",
                                                        HistoricalPrice.COLLECTION,
                                                        TIME_FIELD, META_FIELD);
                                        return mongoTemplate.getMongoDatabase()
                                                        .flatMap(db -> Mono.from(db.createCollection(
                                                                        HistoricalPrice.COLLECTION, options)))
                                                        .then(ensureIndexes);
                                })
                                .doOnError(
                                                error -> LOG.error("Failed to ensure time-series collection {}: {}",
                                                                HistoricalPrice.COLLECTION,
                                                                error.getMessage(), error))
                                .onErrorResume(error -> Mono.empty())
                                .subscribe();
        }
}
