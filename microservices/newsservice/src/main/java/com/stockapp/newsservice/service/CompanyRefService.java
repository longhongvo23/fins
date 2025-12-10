package com.stockapp.newsservice.service;

import com.stockapp.newsservice.repository.CompanyRefRepository;
import com.stockapp.newsservice.service.dto.CompanyRefDTO;
import com.stockapp.newsservice.service.mapper.CompanyRefMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.newsservice.domain.CompanyRef}.
 */
@Service
public class CompanyRefService {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyRefService.class);

    private final CompanyRefRepository companyRefRepository;

    private final CompanyRefMapper companyRefMapper;

    public CompanyRefService(CompanyRefRepository companyRefRepository, CompanyRefMapper companyRefMapper) {
        this.companyRefRepository = companyRefRepository;
        this.companyRefMapper = companyRefMapper;
    }

    /**
     * Save a companyRef.
     *
     * @param companyRefDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CompanyRefDTO> save(CompanyRefDTO companyRefDTO) {
        LOG.debug("Request to save CompanyRef : {}", companyRefDTO);
        return companyRefRepository.save(companyRefMapper.toEntity(companyRefDTO)).map(companyRefMapper::toDto);
    }

    /**
     * Update a companyRef.
     *
     * @param companyRefDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CompanyRefDTO> update(CompanyRefDTO companyRefDTO) {
        LOG.debug("Request to update CompanyRef : {}", companyRefDTO);
        return companyRefRepository.save(companyRefMapper.toEntity(companyRefDTO)).map(companyRefMapper::toDto);
    }

    /**
     * Partially update a companyRef.
     *
     * @param companyRefDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CompanyRefDTO> partialUpdate(CompanyRefDTO companyRefDTO) {
        LOG.debug("Request to partially update CompanyRef : {}", companyRefDTO);

        return companyRefRepository
            .findById(companyRefDTO.getId())
            .map(existingCompanyRef -> {
                companyRefMapper.partialUpdate(existingCompanyRef, companyRefDTO);

                return existingCompanyRef;
            })
            .flatMap(companyRefRepository::save)
            .map(companyRefMapper::toDto);
    }

    /**
     * Get all the companyRefs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<CompanyRefDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CompanyRefs");
        return companyRefRepository.findAllBy(pageable).map(companyRefMapper::toDto);
    }

    /**
     * Returns the number of companyRefs available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return companyRefRepository.count();
    }

    /**
     * Get one companyRef by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<CompanyRefDTO> findOne(String id) {
        LOG.debug("Request to get CompanyRef : {}", id);
        return companyRefRepository.findById(id).map(companyRefMapper::toDto);
    }

    /**
     * Delete the companyRef by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete CompanyRef : {}", id);
        return companyRefRepository.deleteById(id);
    }
}
