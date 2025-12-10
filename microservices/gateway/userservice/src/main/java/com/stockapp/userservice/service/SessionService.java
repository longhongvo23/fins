package com.stockapp.userservice.service;

import com.stockapp.userservice.repository.SessionRepository;
import com.stockapp.userservice.service.dto.SessionDTO;
import com.stockapp.userservice.service.mapper.SessionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.userservice.domain.Session}.
 */
@Service
public class SessionService {

    private static final Logger LOG = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;

    private final SessionMapper sessionMapper;

    public SessionService(SessionRepository sessionRepository, SessionMapper sessionMapper) {
        this.sessionRepository = sessionRepository;
        this.sessionMapper = sessionMapper;
    }

    /**
     * Save a session.
     *
     * @param sessionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SessionDTO> save(SessionDTO sessionDTO) {
        LOG.debug("Request to save Session : {}", sessionDTO);
        return sessionRepository.save(sessionMapper.toEntity(sessionDTO)).map(sessionMapper::toDto);
    }

    /**
     * Update a session.
     *
     * @param sessionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SessionDTO> update(SessionDTO sessionDTO) {
        LOG.debug("Request to update Session : {}", sessionDTO);
        return sessionRepository.save(sessionMapper.toEntity(sessionDTO)).map(sessionMapper::toDto);
    }

    /**
     * Partially update a session.
     *
     * @param sessionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SessionDTO> partialUpdate(SessionDTO sessionDTO) {
        LOG.debug("Request to partially update Session : {}", sessionDTO);

        return sessionRepository
            .findById(sessionDTO.getId())
            .map(existingSession -> {
                sessionMapper.partialUpdate(existingSession, sessionDTO);

                return existingSession;
            })
            .flatMap(sessionRepository::save)
            .map(sessionMapper::toDto);
    }

    /**
     * Get all the sessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<SessionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Sessions");
        return sessionRepository.findAllBy(pageable).map(sessionMapper::toDto);
    }

    /**
     * Get all the sessions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<SessionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return sessionRepository.findAllWithEagerRelationships(pageable).map(sessionMapper::toDto);
    }

    /**
     * Returns the number of sessions available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return sessionRepository.count();
    }

    /**
     * Get one session by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<SessionDTO> findOne(String id) {
        LOG.debug("Request to get Session : {}", id);
        return sessionRepository.findOneWithEagerRelationships(id).map(sessionMapper::toDto);
    }

    /**
     * Delete the session by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Session : {}", id);
        return sessionRepository.deleteById(id);
    }
}
