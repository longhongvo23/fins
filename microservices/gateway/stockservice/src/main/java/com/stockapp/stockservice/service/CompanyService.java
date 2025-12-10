package com.stockapp.stockservice.service;

import com.stockapp.stockservice.repository.CompanyRepository;
import com.stockapp.stockservice.service.dto.CompanyDTO;
import com.stockapp.stockservice.service.mapper.CompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.stockservice.domain.Company}.
 */
@Service
public class CompanyService {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CompanyDTO> save(CompanyDTO companyDTO) {
        LOG.debug("Request to save Company : {}", companyDTO);
        return companyRepository.save(companyMapper.toEntity(companyDTO)).map(companyMapper::toDto);
    }

    /**
     * Update a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CompanyDTO> update(CompanyDTO companyDTO) {
        LOG.debug("Request to update Company : {}", companyDTO);
        return companyRepository.save(companyMapper.toEntity(companyDTO)).map(companyMapper::toDto);
    }

    /**
     * Partially update a company.
     *
     * @param companyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CompanyDTO> partialUpdate(CompanyDTO companyDTO) {
        LOG.debug("Request to partially update Company : {}", companyDTO);

        return companyRepository
            .findById(companyDTO.getId())
            .map(existingCompany -> {
                companyMapper.partialUpdate(existingCompany, companyDTO);

                return existingCompany;
            })
            .flatMap(companyRepository::save)
            .map(companyMapper::toDto);
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<CompanyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Companies");
        return companyRepository.findAllBy(pageable).map(companyMapper::toDto);
    }

    /**
     * Returns the number of companies available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return companyRepository.count();
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<CompanyDTO> findOne(String id) {
        LOG.debug("Request to get Company : {}", id);
        return companyRepository.findById(id).map(companyMapper::toDto);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Company : {}", id);
        return companyRepository.deleteById(id);
    }
}
