package com.stockapp.userservice.service;

import com.stockapp.userservice.repository.LoginHistoryRepository;
import com.stockapp.userservice.service.dto.LoginHistoryDTO;
import com.stockapp.userservice.service.mapper.LoginHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.userservice.domain.LoginHistory}.
 */
@Service
public class LoginHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(LoginHistoryService.class);

    private final LoginHistoryRepository loginHistoryRepository;

    private final LoginHistoryMapper loginHistoryMapper;

    public LoginHistoryService(LoginHistoryRepository loginHistoryRepository, LoginHistoryMapper loginHistoryMapper) {
        this.loginHistoryRepository = loginHistoryRepository;
        this.loginHistoryMapper = loginHistoryMapper;
    }

    /**
     * Save a loginHistory.
     *
     * @param loginHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LoginHistoryDTO> save(LoginHistoryDTO loginHistoryDTO) {
        LOG.debug("Request to save LoginHistory : {}", loginHistoryDTO);
        return loginHistoryRepository.save(loginHistoryMapper.toEntity(loginHistoryDTO)).map(loginHistoryMapper::toDto);
    }

    /**
     * Update a loginHistory.
     *
     * @param loginHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LoginHistoryDTO> update(LoginHistoryDTO loginHistoryDTO) {
        LOG.debug("Request to update LoginHistory : {}", loginHistoryDTO);
        return loginHistoryRepository.save(loginHistoryMapper.toEntity(loginHistoryDTO)).map(loginHistoryMapper::toDto);
    }

    /**
     * Partially update a loginHistory.
     *
     * @param loginHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<LoginHistoryDTO> partialUpdate(LoginHistoryDTO loginHistoryDTO) {
        LOG.debug("Request to partially update LoginHistory : {}", loginHistoryDTO);

        return loginHistoryRepository
            .findById(loginHistoryDTO.getId())
            .map(existingLoginHistory -> {
                loginHistoryMapper.partialUpdate(existingLoginHistory, loginHistoryDTO);

                return existingLoginHistory;
            })
            .flatMap(loginHistoryRepository::save)
            .map(loginHistoryMapper::toDto);
    }

    /**
     * Get all the loginHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<LoginHistoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all LoginHistories");
        return loginHistoryRepository.findAllBy(pageable).map(loginHistoryMapper::toDto);
    }

    /**
     * Get all the loginHistories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<LoginHistoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return loginHistoryRepository.findAllWithEagerRelationships(pageable).map(loginHistoryMapper::toDto);
    }

    /**
     * Returns the number of loginHistories available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return loginHistoryRepository.count();
    }

    /**
     * Get one loginHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<LoginHistoryDTO> findOne(String id) {
        LOG.debug("Request to get LoginHistory : {}", id);
        return loginHistoryRepository.findOneWithEagerRelationships(id).map(loginHistoryMapper::toDto);
    }

    /**
     * Delete the loginHistory by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete LoginHistory : {}", id);
        return loginHistoryRepository.deleteById(id);
    }
}
