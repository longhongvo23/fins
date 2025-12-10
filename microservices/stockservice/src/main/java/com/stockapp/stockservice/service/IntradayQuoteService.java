package com.stockapp.stockservice.service;

import com.stockapp.stockservice.repository.IntradayQuoteRepository;
import com.stockapp.stockservice.service.dto.IntradayQuoteDTO;
import com.stockapp.stockservice.service.mapper.IntradayQuoteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.stockapp.stockservice.domain.IntradayQuote}.
 */
@Service
public class IntradayQuoteService {

    private static final Logger LOG = LoggerFactory.getLogger(IntradayQuoteService.class);

    private final IntradayQuoteRepository intradayQuoteRepository;

    private final IntradayQuoteMapper intradayQuoteMapper;

    public IntradayQuoteService(IntradayQuoteRepository intradayQuoteRepository,
            IntradayQuoteMapper intradayQuoteMapper) {
        this.intradayQuoteRepository = intradayQuoteRepository;
        this.intradayQuoteMapper = intradayQuoteMapper;
    }

    /**
     * Save a intradayQuote.
     *
     * @param intradayQuoteDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IntradayQuoteDTO> save(IntradayQuoteDTO intradayQuoteDTO) {
        LOG.debug("Request to save IntradayQuote : {}", intradayQuoteDTO);
        return intradayQuoteRepository.save(intradayQuoteMapper.toEntity(intradayQuoteDTO))
                .map(intradayQuoteMapper::toDto);
    }

    /**
     * Update a intradayQuote.
     *
     * @param intradayQuoteDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IntradayQuoteDTO> update(IntradayQuoteDTO intradayQuoteDTO) {
        LOG.debug("Request to update IntradayQuote : {}", intradayQuoteDTO);
        return intradayQuoteRepository.save(intradayQuoteMapper.toEntity(intradayQuoteDTO))
                .map(intradayQuoteMapper::toDto);
    }

    /**
     * Partially update a intradayQuote.
     *
     * @param intradayQuoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<IntradayQuoteDTO> partialUpdate(IntradayQuoteDTO intradayQuoteDTO) {
        LOG.debug("Request to partially update IntradayQuote : {}", intradayQuoteDTO);

        return intradayQuoteRepository
                .findById(intradayQuoteDTO.getId())
                .map(existingIntradayQuote -> {
                    intradayQuoteMapper.partialUpdate(existingIntradayQuote, intradayQuoteDTO);

                    return existingIntradayQuote;
                })
                .flatMap(intradayQuoteRepository::save)
                .map(intradayQuoteMapper::toDto);
    }

    /**
     * Get all the intradayQuotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<IntradayQuoteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all IntradayQuotes");
        return intradayQuoteRepository.findAllBy(pageable).map(intradayQuoteMapper::toDto);
    }

    /**
     * Returns the number of intradayQuotes available.
     * 
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return intradayQuoteRepository.count();
    }

    /**
     * Get one intradayQuote by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<IntradayQuoteDTO> findOne(String id) {
        LOG.debug("Request to get IntradayQuote : {}", id);
        return intradayQuoteRepository.findById(id).map(intradayQuoteMapper::toDto);
    }

    /**
     * Delete the intradayQuote by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete IntradayQuote : {}", id);
        return intradayQuoteRepository.deleteById(id);
    }

    /**
     * Find intradayQuote by symbol
     *
     * @param symbol the stock symbol
     * @return the entity
     */
    public Mono<IntradayQuoteDTO> findBySymbol(String symbol) {
        LOG.debug("Request to get IntradayQuote by symbol : {}", symbol);
        return intradayQuoteRepository.findBySymbol(symbol).map(intradayQuoteMapper::toDto);
    }
}
