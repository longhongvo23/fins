package com.stockapp.stockservice.web.rest;

import com.stockapp.stockservice.repository.RecommendationRepository;
import com.stockapp.stockservice.service.RecommendationService;
import com.stockapp.stockservice.service.dto.RecommendationDTO;
import com.stockapp.stockservice.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.stockapp.stockservice.domain.Recommendation}.
 */
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationResource.class);

    private static final String ENTITY_NAME = "stockserviceRecommendation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecommendationService recommendationService;

    private final RecommendationRepository recommendationRepository;

    public RecommendationResource(RecommendationService recommendationService, RecommendationRepository recommendationRepository) {
        this.recommendationService = recommendationService;
        this.recommendationRepository = recommendationRepository;
    }

    /**
     * {@code POST  /recommendations} : Create a new recommendation.
     *
     * @param recommendationDTO the recommendationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recommendationDTO, or with status {@code 400 (Bad Request)} if the recommendation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<RecommendationDTO>> createRecommendation(@Valid @RequestBody RecommendationDTO recommendationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Recommendation : {}", recommendationDTO);
        if (recommendationDTO.getId() != null) {
            throw new BadRequestAlertException("A new recommendation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return recommendationService
            .save(recommendationDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/recommendations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /recommendations/:id} : Updates an existing recommendation.
     *
     * @param id the id of the recommendationDTO to save.
     * @param recommendationDTO the recommendationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recommendationDTO,
     * or with status {@code 400 (Bad Request)} if the recommendationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recommendationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<RecommendationDTO>> updateRecommendation(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody RecommendationDTO recommendationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Recommendation : {}, {}", id, recommendationDTO);
        if (recommendationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recommendationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return recommendationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return recommendationService
                    .update(recommendationDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /recommendations/:id} : Partial updates given fields of an existing recommendation, field will ignore if it is null
     *
     * @param id the id of the recommendationDTO to save.
     * @param recommendationDTO the recommendationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recommendationDTO,
     * or with status {@code 400 (Bad Request)} if the recommendationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recommendationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recommendationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RecommendationDTO>> partialUpdateRecommendation(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody RecommendationDTO recommendationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Recommendation partially : {}, {}", id, recommendationDTO);
        if (recommendationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recommendationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return recommendationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RecommendationDTO> result = recommendationService.partialUpdate(recommendationDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /recommendations} : get all the recommendations.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recommendations in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<RecommendationDTO>>> getAllRecommendations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Recommendations");
        return recommendationService
            .countAll()
            .zipWith(recommendationService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /recommendations/:id} : get the "id" recommendation.
     *
     * @param id the id of the recommendationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recommendationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<RecommendationDTO>> getRecommendation(@PathVariable("id") String id) {
        LOG.debug("REST request to get Recommendation : {}", id);
        Mono<RecommendationDTO> recommendationDTO = recommendationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recommendationDTO);
    }

    /**
     * {@code DELETE  /recommendations/:id} : delete the "id" recommendation.
     *
     * @param id the id of the recommendationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRecommendation(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Recommendation : {}", id);
        return recommendationService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
