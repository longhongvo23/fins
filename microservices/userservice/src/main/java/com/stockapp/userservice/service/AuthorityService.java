package com.stockapp.userservice.service;

import com.stockapp.userservice.repository.AuthorityRepository;
import com.stockapp.userservice.service.dto.AuthorityDTO;
import com.stockapp.userservice.service.mapper.AuthorityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.userservice.domain.Authority}.
 */
@Service
public class AuthorityService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorityService.class);

    private final AuthorityRepository authorityRepository;

    private final AuthorityMapper authorityMapper;

    public AuthorityService(AuthorityRepository authorityRepository, AuthorityMapper authorityMapper) {
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
    }

    /**
     * Save a authority.
     *
     * @param authorityDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AuthorityDTO> save(AuthorityDTO authorityDTO) {
        LOG.debug("Request to save Authority : {}", authorityDTO);
        return authorityRepository.save(authorityMapper.toEntity(authorityDTO)).map(authorityMapper::toDto);
    }

    /**
     * Update a authority.
     *
     * @param authorityDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AuthorityDTO> update(AuthorityDTO authorityDTO) {
        LOG.debug("Request to update Authority : {}", authorityDTO);
        return authorityRepository.save(authorityMapper.toEntity(authorityDTO)).map(authorityMapper::toDto);
    }

    /**
     * Partially update a authority.
     *
     * @param authorityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AuthorityDTO> partialUpdate(AuthorityDTO authorityDTO) {
        LOG.debug("Request to partially update Authority : {}", authorityDTO);

        return authorityRepository
            .findById(authorityDTO.getId())
            .map(existingAuthority -> {
                authorityMapper.partialUpdate(existingAuthority, authorityDTO);

                return existingAuthority;
            })
            .flatMap(authorityRepository::save)
            .map(authorityMapper::toDto);
    }

    /**
     * Get all the authorities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<AuthorityDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Authorities");
        return authorityRepository.findAllBy(pageable).map(authorityMapper::toDto);
    }

    /**
     * Returns the number of authorities available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return authorityRepository.count();
    }

    /**
     * Get one authority by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<AuthorityDTO> findOne(String id) {
        LOG.debug("Request to get Authority : {}", id);
        return authorityRepository.findById(id).map(authorityMapper::toDto);
    }

    /**
     * Delete the authority by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Authority : {}", id);
        return authorityRepository.deleteById(id);
    }
}
