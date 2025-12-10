package com.stockapp.stockservice.service;

import com.stockapp.stockservice.repository.StockStatisticsRepository;
import com.stockapp.stockservice.service.dto.StockStatisticsDTO;
import com.stockapp.stockservice.service.mapper.StockStatisticsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.stockservice.domain.StockStatistics}.
 */
@Service
public class StockStatisticsService {

    private static final Logger LOG = LoggerFactory.getLogger(StockStatisticsService.class);

    private final StockStatisticsRepository stockStatisticsRepository;

    private final StockStatisticsMapper stockStatisticsMapper;

    public StockStatisticsService(StockStatisticsRepository stockStatisticsRepository, StockStatisticsMapper stockStatisticsMapper) {
        this.stockStatisticsRepository = stockStatisticsRepository;
        this.stockStatisticsMapper = stockStatisticsMapper;
    }

    /**
     * Save a stockStatistics.
     *
     * @param stockStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<StockStatisticsDTO> save(StockStatisticsDTO stockStatisticsDTO) {
        LOG.debug("Request to save StockStatistics : {}", stockStatisticsDTO);
        return stockStatisticsRepository.save(stockStatisticsMapper.toEntity(stockStatisticsDTO)).map(stockStatisticsMapper::toDto);
    }

    /**
     * Update a stockStatistics.
     *
     * @param stockStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<StockStatisticsDTO> update(StockStatisticsDTO stockStatisticsDTO) {
        LOG.debug("Request to update StockStatistics : {}", stockStatisticsDTO);
        return stockStatisticsRepository.save(stockStatisticsMapper.toEntity(stockStatisticsDTO)).map(stockStatisticsMapper::toDto);
    }

    /**
     * Partially update a stockStatistics.
     *
     * @param stockStatisticsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<StockStatisticsDTO> partialUpdate(StockStatisticsDTO stockStatisticsDTO) {
        LOG.debug("Request to partially update StockStatistics : {}", stockStatisticsDTO);

        return stockStatisticsRepository
            .findById(stockStatisticsDTO.getId())
            .map(existingStockStatistics -> {
                stockStatisticsMapper.partialUpdate(existingStockStatistics, stockStatisticsDTO);

                return existingStockStatistics;
            })
            .flatMap(stockStatisticsRepository::save)
            .map(stockStatisticsMapper::toDto);
    }

    /**
     * Get all the stockStatistics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<StockStatisticsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all StockStatistics");
        return stockStatisticsRepository.findAllBy(pageable).map(stockStatisticsMapper::toDto);
    }

    /**
     * Returns the number of stockStatistics available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return stockStatisticsRepository.count();
    }

    /**
     * Get one stockStatistics by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<StockStatisticsDTO> findOne(String id) {
        LOG.debug("Request to get StockStatistics : {}", id);
        return stockStatisticsRepository.findById(id).map(stockStatisticsMapper::toDto);
    }

    /**
     * Delete the stockStatistics by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete StockStatistics : {}", id);
        return stockStatisticsRepository.deleteById(id);
    }
}
