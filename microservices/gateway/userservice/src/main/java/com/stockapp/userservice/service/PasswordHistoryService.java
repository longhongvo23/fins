package com.stockapp.userservice.service;

import com.stockapp.userservice.repository.PasswordHistoryRepository;
import com.stockapp.userservice.service.dto.PasswordHistoryDTO;
import com.stockapp.userservice.service.mapper.PasswordHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.userservice.domain.PasswordHistory}.
 */
@Service
public class PasswordHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordHistoryService.class);

    private final PasswordHistoryRepository passwordHistoryRepository;

    private final PasswordHistoryMapper passwordHistoryMapper;

    public PasswordHistoryService(PasswordHistoryRepository passwordHistoryRepository, PasswordHistoryMapper passwordHistoryMapper) {
        this.passwordHistoryRepository = passwordHistoryRepository;
        this.passwordHistoryMapper = passwordHistoryMapper;
    }

    /**
     * Save a passwordHistory.
     *
     * @param passwordHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PasswordHistoryDTO> save(PasswordHistoryDTO passwordHistoryDTO) {
        LOG.debug("Request to save PasswordHistory : {}", passwordHistoryDTO);
        return passwordHistoryRepository.save(passwordHistoryMapper.toEntity(passwordHistoryDTO)).map(passwordHistoryMapper::toDto);
    }

    /**
     * Update a passwordHistory.
     *
     * @param passwordHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PasswordHistoryDTO> update(PasswordHistoryDTO passwordHistoryDTO) {
        LOG.debug("Request to update PasswordHistory : {}", passwordHistoryDTO);
        return passwordHistoryRepository.save(passwordHistoryMapper.toEntity(passwordHistoryDTO)).map(passwordHistoryMapper::toDto);
    }

    /**
     * Partially update a passwordHistory.
     *
     * @param passwordHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PasswordHistoryDTO> partialUpdate(PasswordHistoryDTO passwordHistoryDTO) {
        LOG.debug("Request to partially update PasswordHistory : {}", passwordHistoryDTO);

        return passwordHistoryRepository
            .findById(passwordHistoryDTO.getId())
            .map(existingPasswordHistory -> {
                passwordHistoryMapper.partialUpdate(existingPasswordHistory, passwordHistoryDTO);

                return existingPasswordHistory;
            })
            .flatMap(passwordHistoryRepository::save)
            .map(passwordHistoryMapper::toDto);
    }

    /**
     * Get all the passwordHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<PasswordHistoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PasswordHistories");
        return passwordHistoryRepository.findAllBy(pageable).map(passwordHistoryMapper::toDto);
    }

    /**
     * Get all the passwordHistories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<PasswordHistoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return passwordHistoryRepository.findAllWithEagerRelationships(pageable).map(passwordHistoryMapper::toDto);
    }

    /**
     * Returns the number of passwordHistories available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return passwordHistoryRepository.count();
    }

    /**
     * Get one passwordHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<PasswordHistoryDTO> findOne(String id) {
        LOG.debug("Request to get PasswordHistory : {}", id);
        return passwordHistoryRepository.findOneWithEagerRelationships(id).map(passwordHistoryMapper::toDto);
    }

    /**
     * Delete the passwordHistory by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete PasswordHistory : {}", id);
        return passwordHistoryRepository.deleteById(id);
    }
}
