package com.stockapp.stockservice.service;

import com.stockapp.stockservice.repository.HistoricalPriceRepository;
import com.stockapp.stockservice.service.dto.HistoricalPriceDTO;
import com.stockapp.stockservice.service.mapper.HistoricalPriceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.stockapp.stockservice.domain.HistoricalPrice}.
 */
@Service
public class HistoricalPriceService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoricalPriceService.class);

    private final HistoricalPriceRepository historicalPriceRepository;

    private final HistoricalPriceMapper historicalPriceMapper;

    public HistoricalPriceService(HistoricalPriceRepository historicalPriceRepository,
            HistoricalPriceMapper historicalPriceMapper) {
        this.historicalPriceRepository = historicalPriceRepository;
        this.historicalPriceMapper = historicalPriceMapper;
    }

    /**
     * Save a historicalPrice.
     *
     * @param historicalPriceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<HistoricalPriceDTO> save(HistoricalPriceDTO historicalPriceDTO) {
        LOG.debug("Request to save HistoricalPrice : {}", historicalPriceDTO);
        return historicalPriceRepository.save(historicalPriceMapper.toEntity(historicalPriceDTO))
                .map(historicalPriceMapper::toDto);
    }

    /**
     * Update a historicalPrice.
     *
     * @param historicalPriceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<HistoricalPriceDTO> update(HistoricalPriceDTO historicalPriceDTO) {
        LOG.debug("Request to update HistoricalPrice : {}", historicalPriceDTO);
        return historicalPriceRepository.save(historicalPriceMapper.toEntity(historicalPriceDTO))
                .map(historicalPriceMapper::toDto);
    }

    /**
     * Partially update a historicalPrice.
     *
     * @param historicalPriceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<HistoricalPriceDTO> partialUpdate(HistoricalPriceDTO historicalPriceDTO) {
        LOG.debug("Request to partially update HistoricalPrice : {}", historicalPriceDTO);

        return historicalPriceRepository
                .findById(historicalPriceDTO.getId())
                .map(existingHistoricalPrice -> {
                    historicalPriceMapper.partialUpdate(existingHistoricalPrice, historicalPriceDTO);

                    return existingHistoricalPrice;
                })
                .flatMap(historicalPriceRepository::save)
                .map(historicalPriceMapper::toDto);
    }

    /**
     * Get all the historicalPrices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<HistoricalPriceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all HistoricalPrices");
        return historicalPriceRepository.findAllBy(pageable).map(historicalPriceMapper::toDto);
    }

    /**
     * Get all the historicalPrices with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<HistoricalPriceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return historicalPriceRepository.findAllWithEagerRelationships(pageable).map(historicalPriceMapper::toDto);
    }

    /**
     * Returns the number of historicalPrices available.
     * 
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return historicalPriceRepository.count();
    }

    /**
     * Get historical prices by symbol.
     *
     * @param symbol   the symbol to filter by.
     * @param pageable the pagination information.
     * @return the list of entities for the symbol.
     */
    public Flux<HistoricalPriceDTO> findBySymbol(String symbol, Pageable pageable) {
        LOG.debug("Request to get HistoricalPrices for symbol: {}", symbol);
        return historicalPriceRepository.findBySymbol(symbol, pageable).map(historicalPriceMapper::toDto);
    }

    /**
     * Get latest historical price by symbol.
     *
     * @param symbol the stock symbol.
     * @return the latest price entity.
     */
    public Mono<HistoricalPriceDTO> findLatestBySymbol(String symbol) {
        LOG.debug("Request to get latest HistoricalPrice for symbol: {}", symbol);
        return historicalPriceRepository.findBySymbolOrderByDatetimeDesc(symbol)
                .next()
                .map(historicalPriceMapper::toDto);
    }

    /**
     * Get previous (second latest) historical price by symbol.
     *
     * @param symbol the stock symbol.
     * @return the previous price entity.
     */
    public Mono<HistoricalPriceDTO> findPreviousBySymbol(String symbol) {
        LOG.debug("Request to get previous HistoricalPrice for symbol: {}", symbol);
        return historicalPriceRepository.findBySymbolOrderByDatetimeDesc(symbol)
                .skip(1)
                .next()
                .map(historicalPriceMapper::toDto);
    }

    /**
     * Get historical prices by symbol and date range.
     *
     * @param symbol   the stock symbol.
     * @param fromDate start date (YYYY-MM-DD).
     * @param toDate   end date (YYYY-MM-DD).
     * @param pageable pagination information.
     * @return the list of prices in date range.
     */
    public Flux<HistoricalPriceDTO> findBySymbolAndDateRange(
            String symbol,
            String fromDate,
            String toDate,
            Pageable pageable) {
        LOG.debug("Request to get HistoricalPrices for {} from {} to {}", symbol, fromDate, toDate);

        var from = parseDateToStartOfDay(fromDate);
        var to = parseDateToEndOfDay(toDate);

        return historicalPriceRepository.findBySymbolAndDatetimeBetween(symbol, from, to, pageable)
                .map(historicalPriceMapper::toDto);
    }

    private java.time.Instant parseDateToStartOfDay(String date) {
        try {
            return java.time.LocalDate.parse(date, java.time.format.DateTimeFormatter.ISO_DATE)
                    .atStartOfDay(java.time.ZoneOffset.UTC)
                    .toInstant();
        } catch (Exception e) {
            LOG.warn("Failed to parse fromDate '{}', using epoch start", date, e);
            return java.time.Instant.EPOCH;
        }
    }

    private java.time.Instant parseDateToEndOfDay(String date) {
        try {
            return java.time.LocalDate.parse(date, java.time.format.DateTimeFormatter.ISO_DATE)
                    .atTime(23, 59, 59)
                    .atZone(java.time.ZoneOffset.UTC)
                    .toInstant();
        } catch (Exception e) {
            LOG.warn("Failed to parse toDate '{}', using now", date, e);
            return java.time.Instant.now();
        }
    }

    /**
     * Get one historicalPrice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<HistoricalPriceDTO> findOne(String id) {
        LOG.debug("Request to get HistoricalPrice : {}", id);
        return historicalPriceRepository.findOneWithEagerRelationships(id).map(historicalPriceMapper::toDto);
    }

    /**
     * Delete the historicalPrice by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete HistoricalPrice : {}", id);
        return historicalPriceRepository.deleteById(id);
    }
}
