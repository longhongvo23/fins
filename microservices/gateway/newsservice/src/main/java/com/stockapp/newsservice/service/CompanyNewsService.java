package com.stockapp.newsservice.service;

import com.stockapp.newsservice.repository.CompanyNewsRepository;
import com.stockapp.newsservice.service.dto.CompanyNewsDTO;
import com.stockapp.newsservice.service.mapper.CompanyNewsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.newsservice.domain.CompanyNews}.
 */
@Service
public class CompanyNewsService {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyNewsService.class);

    private final CompanyNewsRepository companyNewsRepository;

    private final CompanyNewsMapper companyNewsMapper;

    public CompanyNewsService(CompanyNewsRepository companyNewsRepository, CompanyNewsMapper companyNewsMapper) {
        this.companyNewsRepository = companyNewsRepository;
        this.companyNewsMapper = companyNewsMapper;
    }

    /**
     * Save a companyNews.
     *
     * @param companyNewsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CompanyNewsDTO> save(CompanyNewsDTO companyNewsDTO) {
        LOG.debug("Request to save CompanyNews : {}", companyNewsDTO);
        return companyNewsRepository.save(companyNewsMapper.toEntity(companyNewsDTO)).map(companyNewsMapper::toDto);
    }

    /**
     * Update a companyNews.
     *
     * @param companyNewsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CompanyNewsDTO> update(CompanyNewsDTO companyNewsDTO) {
        LOG.debug("Request to update CompanyNews : {}", companyNewsDTO);
        return companyNewsRepository.save(companyNewsMapper.toEntity(companyNewsDTO)).map(companyNewsMapper::toDto);
    }

    /**
     * Partially update a companyNews.
     *
     * @param companyNewsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CompanyNewsDTO> partialUpdate(CompanyNewsDTO companyNewsDTO) {
        LOG.debug("Request to partially update CompanyNews : {}", companyNewsDTO);

        return companyNewsRepository
            .findById(companyNewsDTO.getId())
            .map(existingCompanyNews -> {
                companyNewsMapper.partialUpdate(existingCompanyNews, companyNewsDTO);

                return existingCompanyNews;
            })
            .flatMap(companyNewsRepository::save)
            .map(companyNewsMapper::toDto);
    }

    /**
     * Get all the companyNews.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<CompanyNewsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CompanyNews");
        return companyNewsRepository.findAllBy(pageable).map(companyNewsMapper::toDto);
    }

    /**
     * Get all the companyNews with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<CompanyNewsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return companyNewsRepository.findAllWithEagerRelationships(pageable).map(companyNewsMapper::toDto);
    }

    /**
     * Returns the number of companyNews available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return companyNewsRepository.count();
    }

    /**
     * Get one companyNews by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<CompanyNewsDTO> findOne(String id) {
        LOG.debug("Request to get CompanyNews : {}", id);
        return companyNewsRepository.findOneWithEagerRelationships(id).map(companyNewsMapper::toDto);
    }

    /**
     * Delete the companyNews by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete CompanyNews : {}", id);
        return companyNewsRepository.deleteById(id);
    }
}
