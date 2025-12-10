package com.stockapp.crawlservice.service;

import com.stockapp.crawlservice.repository.CrawlJobStateRepository;
import com.stockapp.crawlservice.service.dto.CrawlJobStateDTO;
import com.stockapp.crawlservice.service.mapper.CrawlJobStateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.crawlservice.domain.CrawlJobState}.
 */
@Service
public class CrawlJobStateService {

    private static final Logger LOG = LoggerFactory.getLogger(CrawlJobStateService.class);

    private final CrawlJobStateRepository crawlJobStateRepository;

    private final CrawlJobStateMapper crawlJobStateMapper;

    public CrawlJobStateService(CrawlJobStateRepository crawlJobStateRepository, CrawlJobStateMapper crawlJobStateMapper) {
        this.crawlJobStateRepository = crawlJobStateRepository;
        this.crawlJobStateMapper = crawlJobStateMapper;
    }

    /**
     * Save a crawlJobState.
     *
     * @param crawlJobStateDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CrawlJobStateDTO> save(CrawlJobStateDTO crawlJobStateDTO) {
        LOG.debug("Request to save CrawlJobState : {}", crawlJobStateDTO);
        return crawlJobStateRepository.save(crawlJobStateMapper.toEntity(crawlJobStateDTO)).map(crawlJobStateMapper::toDto);
    }

    /**
     * Update a crawlJobState.
     *
     * @param crawlJobStateDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CrawlJobStateDTO> update(CrawlJobStateDTO crawlJobStateDTO) {
        LOG.debug("Request to update CrawlJobState : {}", crawlJobStateDTO);
        return crawlJobStateRepository.save(crawlJobStateMapper.toEntity(crawlJobStateDTO)).map(crawlJobStateMapper::toDto);
    }

    /**
     * Partially update a crawlJobState.
     *
     * @param crawlJobStateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CrawlJobStateDTO> partialUpdate(CrawlJobStateDTO crawlJobStateDTO) {
        LOG.debug("Request to partially update CrawlJobState : {}", crawlJobStateDTO);

        return crawlJobStateRepository
            .findById(crawlJobStateDTO.getId())
            .map(existingCrawlJobState -> {
                crawlJobStateMapper.partialUpdate(existingCrawlJobState, crawlJobStateDTO);

                return existingCrawlJobState;
            })
            .flatMap(crawlJobStateRepository::save)
            .map(crawlJobStateMapper::toDto);
    }

    /**
     * Get all the crawlJobStates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<CrawlJobStateDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CrawlJobStates");
        return crawlJobStateRepository.findAllBy(pageable).map(crawlJobStateMapper::toDto);
    }

    /**
     * Returns the number of crawlJobStates available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return crawlJobStateRepository.count();
    }

    /**
     * Get one crawlJobState by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<CrawlJobStateDTO> findOne(String id) {
        LOG.debug("Request to get CrawlJobState : {}", id);
        return crawlJobStateRepository.findById(id).map(crawlJobStateMapper::toDto);
    }

    /**
     * Delete the crawlJobState by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete CrawlJobState : {}", id);
        return crawlJobStateRepository.deleteById(id);
    }
}
