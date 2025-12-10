package com.stockapp.userservice.service;

import com.stockapp.userservice.repository.WatchlistItemRepository;
import com.stockapp.userservice.service.dto.WatchlistItemDTO;
import com.stockapp.userservice.service.mapper.WatchlistItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.userservice.domain.WatchlistItem}.
 */
@Service
public class WatchlistItemService {

    private static final Logger LOG = LoggerFactory.getLogger(WatchlistItemService.class);

    private final WatchlistItemRepository watchlistItemRepository;

    private final WatchlistItemMapper watchlistItemMapper;

    public WatchlistItemService(WatchlistItemRepository watchlistItemRepository, WatchlistItemMapper watchlistItemMapper) {
        this.watchlistItemRepository = watchlistItemRepository;
        this.watchlistItemMapper = watchlistItemMapper;
    }

    /**
     * Save a watchlistItem.
     *
     * @param watchlistItemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WatchlistItemDTO> save(WatchlistItemDTO watchlistItemDTO) {
        LOG.debug("Request to save WatchlistItem : {}", watchlistItemDTO);
        return watchlistItemRepository.save(watchlistItemMapper.toEntity(watchlistItemDTO)).map(watchlistItemMapper::toDto);
    }

    /**
     * Update a watchlistItem.
     *
     * @param watchlistItemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WatchlistItemDTO> update(WatchlistItemDTO watchlistItemDTO) {
        LOG.debug("Request to update WatchlistItem : {}", watchlistItemDTO);
        return watchlistItemRepository.save(watchlistItemMapper.toEntity(watchlistItemDTO)).map(watchlistItemMapper::toDto);
    }

    /**
     * Partially update a watchlistItem.
     *
     * @param watchlistItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WatchlistItemDTO> partialUpdate(WatchlistItemDTO watchlistItemDTO) {
        LOG.debug("Request to partially update WatchlistItem : {}", watchlistItemDTO);

        return watchlistItemRepository
            .findById(watchlistItemDTO.getId())
            .map(existingWatchlistItem -> {
                watchlistItemMapper.partialUpdate(existingWatchlistItem, watchlistItemDTO);

                return existingWatchlistItem;
            })
            .flatMap(watchlistItemRepository::save)
            .map(watchlistItemMapper::toDto);
    }

    /**
     * Get all the watchlistItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<WatchlistItemDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all WatchlistItems");
        return watchlistItemRepository.findAllBy(pageable).map(watchlistItemMapper::toDto);
    }

    /**
     * Get all the watchlistItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<WatchlistItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return watchlistItemRepository.findAllWithEagerRelationships(pageable).map(watchlistItemMapper::toDto);
    }

    /**
     * Returns the number of watchlistItems available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return watchlistItemRepository.count();
    }

    /**
     * Get one watchlistItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<WatchlistItemDTO> findOne(String id) {
        LOG.debug("Request to get WatchlistItem : {}", id);
        return watchlistItemRepository.findOneWithEagerRelationships(id).map(watchlistItemMapper::toDto);
    }

    /**
     * Delete the watchlistItem by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete WatchlistItem : {}", id);
        return watchlistItemRepository.deleteById(id);
    }
}
