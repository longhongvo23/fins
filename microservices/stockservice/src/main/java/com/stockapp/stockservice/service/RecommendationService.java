package com.stockapp.stockservice.service;

import com.stockapp.stockservice.repository.RecommendationRepository;
import com.stockapp.stockservice.service.dto.RecommendationDTO;
import com.stockapp.stockservice.service.mapper.RecommendationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.stockservice.domain.Recommendation}.
 */
@Service
public class RecommendationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationService.class);

    private final RecommendationRepository recommendationRepository;

    private final RecommendationMapper recommendationMapper;

    public RecommendationService(RecommendationRepository recommendationRepository, RecommendationMapper recommendationMapper) {
        this.recommendationRepository = recommendationRepository;
        this.recommendationMapper = recommendationMapper;
    }

    /**
     * Save a recommendation.
     *
     * @param recommendationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RecommendationDTO> save(RecommendationDTO recommendationDTO) {
        LOG.debug("Request to save Recommendation : {}", recommendationDTO);
        return recommendationRepository.save(recommendationMapper.toEntity(recommendationDTO)).map(recommendationMapper::toDto);
    }

    /**
     * Update a recommendation.
     *
     * @param recommendationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RecommendationDTO> update(RecommendationDTO recommendationDTO) {
        LOG.debug("Request to update Recommendation : {}", recommendationDTO);
        return recommendationRepository.save(recommendationMapper.toEntity(recommendationDTO)).map(recommendationMapper::toDto);
    }

    /**
     * Partially update a recommendation.
     *
     * @param recommendationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<RecommendationDTO> partialUpdate(RecommendationDTO recommendationDTO) {
        LOG.debug("Request to partially update Recommendation : {}", recommendationDTO);

        return recommendationRepository
            .findById(recommendationDTO.getId())
            .map(existingRecommendation -> {
                recommendationMapper.partialUpdate(existingRecommendation, recommendationDTO);

                return existingRecommendation;
            })
            .flatMap(recommendationRepository::save)
            .map(recommendationMapper::toDto);
    }

    /**
     * Get all the recommendations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<RecommendationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Recommendations");
        return recommendationRepository.findAllBy(pageable).map(recommendationMapper::toDto);
    }

    /**
     * Get all the recommendations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<RecommendationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return recommendationRepository.findAllWithEagerRelationships(pageable).map(recommendationMapper::toDto);
    }

    /**
     * Returns the number of recommendations available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return recommendationRepository.count();
    }

    /**
     * Get one recommendation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<RecommendationDTO> findOne(String id) {
        LOG.debug("Request to get Recommendation : {}", id);
        return recommendationRepository.findOneWithEagerRelationships(id).map(recommendationMapper::toDto);
    }

    /**
     * Delete the recommendation by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Recommendation : {}", id);
        return recommendationRepository.deleteById(id);
    }
}
