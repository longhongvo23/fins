package com.stockapp.newsservice.service;

import com.stockapp.newsservice.repository.NewsEntityRepository;
import com.stockapp.newsservice.service.dto.NewsEntityDTO;
import com.stockapp.newsservice.service.mapper.NewsEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.newsservice.domain.NewsEntity}.
 */
@Service
public class NewsEntityService {

    private static final Logger LOG = LoggerFactory.getLogger(NewsEntityService.class);

    private final NewsEntityRepository newsEntityRepository;

    private final NewsEntityMapper newsEntityMapper;

    public NewsEntityService(NewsEntityRepository newsEntityRepository, NewsEntityMapper newsEntityMapper) {
        this.newsEntityRepository = newsEntityRepository;
        this.newsEntityMapper = newsEntityMapper;
    }

    /**
     * Save a newsEntity.
     *
     * @param newsEntityDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<NewsEntityDTO> save(NewsEntityDTO newsEntityDTO) {
        LOG.debug("Request to save NewsEntity : {}", newsEntityDTO);
        return newsEntityRepository.save(newsEntityMapper.toEntity(newsEntityDTO)).map(newsEntityMapper::toDto);
    }

    /**
     * Update a newsEntity.
     *
     * @param newsEntityDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<NewsEntityDTO> update(NewsEntityDTO newsEntityDTO) {
        LOG.debug("Request to update NewsEntity : {}", newsEntityDTO);
        return newsEntityRepository.save(newsEntityMapper.toEntity(newsEntityDTO)).map(newsEntityMapper::toDto);
    }

    /**
     * Partially update a newsEntity.
     *
     * @param newsEntityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<NewsEntityDTO> partialUpdate(NewsEntityDTO newsEntityDTO) {
        LOG.debug("Request to partially update NewsEntity : {}", newsEntityDTO);

        return newsEntityRepository
            .findById(newsEntityDTO.getId())
            .map(existingNewsEntity -> {
                newsEntityMapper.partialUpdate(existingNewsEntity, newsEntityDTO);

                return existingNewsEntity;
            })
            .flatMap(newsEntityRepository::save)
            .map(newsEntityMapper::toDto);
    }

    /**
     * Get all the newsEntities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<NewsEntityDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all NewsEntities");
        return newsEntityRepository.findAllBy(pageable).map(newsEntityMapper::toDto);
    }

    /**
     * Get all the newsEntities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<NewsEntityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return newsEntityRepository.findAllWithEagerRelationships(pageable).map(newsEntityMapper::toDto);
    }

    /**
     * Returns the number of newsEntities available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return newsEntityRepository.count();
    }

    /**
     * Get one newsEntity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<NewsEntityDTO> findOne(String id) {
        LOG.debug("Request to get NewsEntity : {}", id);
        return newsEntityRepository.findOneWithEagerRelationships(id).map(newsEntityMapper::toDto);
    }

    /**
     * Delete the newsEntity by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete NewsEntity : {}", id);
        return newsEntityRepository.deleteById(id);
    }
}
