package com.stockapp.newsservice.web.rest;

import com.stockapp.newsservice.repository.NewsEntityRepository;
import com.stockapp.newsservice.service.NewsEntityService;
import com.stockapp.newsservice.service.dto.NewsEntityDTO;
import com.stockapp.newsservice.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.stockapp.newsservice.domain.NewsEntity}.
 */
@RestController
@RequestMapping("/api/news-entities")
public class NewsEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(NewsEntityResource.class);

    private static final String ENTITY_NAME = "newsserviceNewsEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NewsEntityService newsEntityService;

    private final NewsEntityRepository newsEntityRepository;

    public NewsEntityResource(NewsEntityService newsEntityService, NewsEntityRepository newsEntityRepository) {
        this.newsEntityService = newsEntityService;
        this.newsEntityRepository = newsEntityRepository;
    }

    /**
     * {@code POST  /news-entities} : Create a new newsEntity.
     *
     * @param newsEntityDTO the newsEntityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new newsEntityDTO, or with status {@code 400 (Bad Request)} if the newsEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<NewsEntityDTO>> createNewsEntity(@Valid @RequestBody NewsEntityDTO newsEntityDTO) throws URISyntaxException {
        LOG.debug("REST request to save NewsEntity : {}", newsEntityDTO);
        if (newsEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new newsEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return newsEntityService
            .save(newsEntityDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/news-entities/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /news-entities/:id} : Updates an existing newsEntity.
     *
     * @param id the id of the newsEntityDTO to save.
     * @param newsEntityDTO the newsEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated newsEntityDTO,
     * or with status {@code 400 (Bad Request)} if the newsEntityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the newsEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<NewsEntityDTO>> updateNewsEntity(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody NewsEntityDTO newsEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NewsEntity : {}, {}", id, newsEntityDTO);
        if (newsEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, newsEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return newsEntityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return newsEntityService
                    .update(newsEntityDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /news-entities/:id} : Partial updates given fields of an existing newsEntity, field will ignore if it is null
     *
     * @param id the id of the newsEntityDTO to save.
     * @param newsEntityDTO the newsEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated newsEntityDTO,
     * or with status {@code 400 (Bad Request)} if the newsEntityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the newsEntityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the newsEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<NewsEntityDTO>> partialUpdateNewsEntity(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody NewsEntityDTO newsEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NewsEntity partially : {}, {}", id, newsEntityDTO);
        if (newsEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, newsEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return newsEntityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<NewsEntityDTO> result = newsEntityService.partialUpdate(newsEntityDTO);

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
     * {@code GET  /news-entities} : get all the newsEntities.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of newsEntities in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<NewsEntityDTO>>> getAllNewsEntities(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of NewsEntities");
        return newsEntityService
            .countAll()
            .zipWith(newsEntityService.findAll(pageable).collectList())
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
     * {@code GET  /news-entities/:id} : get the "id" newsEntity.
     *
     * @param id the id of the newsEntityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the newsEntityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<NewsEntityDTO>> getNewsEntity(@PathVariable("id") String id) {
        LOG.debug("REST request to get NewsEntity : {}", id);
        Mono<NewsEntityDTO> newsEntityDTO = newsEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(newsEntityDTO);
    }

    /**
     * {@code DELETE  /news-entities/:id} : delete the "id" newsEntity.
     *
     * @param id the id of the newsEntityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteNewsEntity(@PathVariable("id") String id) {
        LOG.debug("REST request to delete NewsEntity : {}", id);
        return newsEntityService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
