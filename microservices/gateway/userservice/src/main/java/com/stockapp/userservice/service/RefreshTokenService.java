package com.stockapp.userservice.service;

import com.stockapp.userservice.repository.RefreshTokenRepository;
import com.stockapp.userservice.service.dto.RefreshTokenDTO;
import com.stockapp.userservice.service.mapper.RefreshTokenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.userservice.domain.RefreshToken}.
 */
@Service
public class RefreshTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(RefreshTokenService.class);

    private final RefreshTokenRepository refreshTokenRepository;

    private final RefreshTokenMapper refreshTokenMapper;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, RefreshTokenMapper refreshTokenMapper) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    /**
     * Save a refreshToken.
     *
     * @param refreshTokenDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RefreshTokenDTO> save(RefreshTokenDTO refreshTokenDTO) {
        LOG.debug("Request to save RefreshToken : {}", refreshTokenDTO);
        return refreshTokenRepository.save(refreshTokenMapper.toEntity(refreshTokenDTO)).map(refreshTokenMapper::toDto);
    }

    /**
     * Update a refreshToken.
     *
     * @param refreshTokenDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RefreshTokenDTO> update(RefreshTokenDTO refreshTokenDTO) {
        LOG.debug("Request to update RefreshToken : {}", refreshTokenDTO);
        return refreshTokenRepository.save(refreshTokenMapper.toEntity(refreshTokenDTO)).map(refreshTokenMapper::toDto);
    }

    /**
     * Partially update a refreshToken.
     *
     * @param refreshTokenDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<RefreshTokenDTO> partialUpdate(RefreshTokenDTO refreshTokenDTO) {
        LOG.debug("Request to partially update RefreshToken : {}", refreshTokenDTO);

        return refreshTokenRepository
            .findById(refreshTokenDTO.getId())
            .map(existingRefreshToken -> {
                refreshTokenMapper.partialUpdate(existingRefreshToken, refreshTokenDTO);

                return existingRefreshToken;
            })
            .flatMap(refreshTokenRepository::save)
            .map(refreshTokenMapper::toDto);
    }

    /**
     * Get all the refreshTokens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<RefreshTokenDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RefreshTokens");
        return refreshTokenRepository.findAllBy(pageable).map(refreshTokenMapper::toDto);
    }

    /**
     * Get all the refreshTokens with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<RefreshTokenDTO> findAllWithEagerRelationships(Pageable pageable) {
        return refreshTokenRepository.findAllWithEagerRelationships(pageable).map(refreshTokenMapper::toDto);
    }

    /**
     * Returns the number of refreshTokens available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return refreshTokenRepository.count();
    }

    /**
     * Get one refreshToken by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<RefreshTokenDTO> findOne(String id) {
        LOG.debug("Request to get RefreshToken : {}", id);
        return refreshTokenRepository.findOneWithEagerRelationships(id).map(refreshTokenMapper::toDto);
    }

    /**
     * Delete the refreshToken by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete RefreshToken : {}", id);
        return refreshTokenRepository.deleteById(id);
    }
}
