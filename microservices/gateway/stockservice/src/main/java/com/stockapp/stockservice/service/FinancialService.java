package com.stockapp.stockservice.service;

import com.stockapp.stockservice.repository.FinancialRepository;
import com.stockapp.stockservice.service.dto.FinancialDTO;
import com.stockapp.stockservice.service.mapper.FinancialMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.stockservice.domain.Financial}.
 */
@Service
public class FinancialService {

    private static final Logger LOG = LoggerFactory.getLogger(FinancialService.class);

    private final FinancialRepository financialRepository;

    private final FinancialMapper financialMapper;

    public FinancialService(FinancialRepository financialRepository, FinancialMapper financialMapper) {
        this.financialRepository = financialRepository;
        this.financialMapper = financialMapper;
    }

    /**
     * Save a financial.
     *
     * @param financialDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FinancialDTO> save(FinancialDTO financialDTO) {
        LOG.debug("Request to save Financial : {}", financialDTO);
        return financialRepository.save(financialMapper.toEntity(financialDTO)).map(financialMapper::toDto);
    }

    /**
     * Update a financial.
     *
     * @param financialDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FinancialDTO> update(FinancialDTO financialDTO) {
        LOG.debug("Request to update Financial : {}", financialDTO);
        return financialRepository.save(financialMapper.toEntity(financialDTO)).map(financialMapper::toDto);
    }

    /**
     * Partially update a financial.
     *
     * @param financialDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FinancialDTO> partialUpdate(FinancialDTO financialDTO) {
        LOG.debug("Request to partially update Financial : {}", financialDTO);

        return financialRepository
            .findById(financialDTO.getId())
            .map(existingFinancial -> {
                financialMapper.partialUpdate(existingFinancial, financialDTO);

                return existingFinancial;
            })
            .flatMap(financialRepository::save)
            .map(financialMapper::toDto);
    }

    /**
     * Get all the financials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<FinancialDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Financials");
        return financialRepository.findAllBy(pageable).map(financialMapper::toDto);
    }

    /**
     * Get all the financials with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<FinancialDTO> findAllWithEagerRelationships(Pageable pageable) {
        return financialRepository.findAllWithEagerRelationships(pageable).map(financialMapper::toDto);
    }

    /**
     * Returns the number of financials available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return financialRepository.count();
    }

    /**
     * Get one financial by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<FinancialDTO> findOne(String id) {
        LOG.debug("Request to get Financial : {}", id);
        return financialRepository.findOneWithEagerRelationships(id).map(financialMapper::toDto);
    }

    /**
     * Delete the financial by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Financial : {}", id);
        return financialRepository.deleteById(id);
    }
}
