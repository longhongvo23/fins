package com.stockapp.stockservice.service;

import com.stockapp.stockservice.repository.PeerCompanyRepository;
import com.stockapp.stockservice.service.dto.PeerCompanyDTO;
import com.stockapp.stockservice.service.mapper.PeerCompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.stockservice.domain.PeerCompany}.
 */
@Service
public class PeerCompanyService {

    private static final Logger LOG = LoggerFactory.getLogger(PeerCompanyService.class);

    private final PeerCompanyRepository peerCompanyRepository;

    private final PeerCompanyMapper peerCompanyMapper;

    public PeerCompanyService(PeerCompanyRepository peerCompanyRepository, PeerCompanyMapper peerCompanyMapper) {
        this.peerCompanyRepository = peerCompanyRepository;
        this.peerCompanyMapper = peerCompanyMapper;
    }

    /**
     * Save a peerCompany.
     *
     * @param peerCompanyDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PeerCompanyDTO> save(PeerCompanyDTO peerCompanyDTO) {
        LOG.debug("Request to save PeerCompany : {}", peerCompanyDTO);
        return peerCompanyRepository.save(peerCompanyMapper.toEntity(peerCompanyDTO)).map(peerCompanyMapper::toDto);
    }

    /**
     * Update a peerCompany.
     *
     * @param peerCompanyDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PeerCompanyDTO> update(PeerCompanyDTO peerCompanyDTO) {
        LOG.debug("Request to update PeerCompany : {}", peerCompanyDTO);
        return peerCompanyRepository.save(peerCompanyMapper.toEntity(peerCompanyDTO)).map(peerCompanyMapper::toDto);
    }

    /**
     * Partially update a peerCompany.
     *
     * @param peerCompanyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PeerCompanyDTO> partialUpdate(PeerCompanyDTO peerCompanyDTO) {
        LOG.debug("Request to partially update PeerCompany : {}", peerCompanyDTO);

        return peerCompanyRepository
            .findById(peerCompanyDTO.getId())
            .map(existingPeerCompany -> {
                peerCompanyMapper.partialUpdate(existingPeerCompany, peerCompanyDTO);

                return existingPeerCompany;
            })
            .flatMap(peerCompanyRepository::save)
            .map(peerCompanyMapper::toDto);
    }

    /**
     * Get all the peerCompanies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<PeerCompanyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PeerCompanies");
        return peerCompanyRepository.findAllBy(pageable).map(peerCompanyMapper::toDto);
    }

    /**
     * Get all the peerCompanies with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<PeerCompanyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return peerCompanyRepository.findAllWithEagerRelationships(pageable).map(peerCompanyMapper::toDto);
    }

    /**
     * Returns the number of peerCompanies available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return peerCompanyRepository.count();
    }

    /**
     * Get one peerCompany by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<PeerCompanyDTO> findOne(String id) {
        LOG.debug("Request to get PeerCompany : {}", id);
        return peerCompanyRepository.findOneWithEagerRelationships(id).map(peerCompanyMapper::toDto);
    }

    /**
     * Delete the peerCompany by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete PeerCompany : {}", id);
        return peerCompanyRepository.deleteById(id);
    }
}
