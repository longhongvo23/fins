package com.stockapp.crawlservice.web.rest;

import com.stockapp.crawlservice.repository.CrawlJobStateRepository;
import com.stockapp.crawlservice.service.CrawlJobStateService;
import com.stockapp.crawlservice.service.dto.CrawlJobStateDTO;
import com.stockapp.crawlservice.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.stockapp.crawlservice.domain.CrawlJobState}.
 */
@RestController
@RequestMapping("/api/crawl-job-states")
public class CrawlJobStateResource {

    private static final Logger LOG = LoggerFactory.getLogger(CrawlJobStateResource.class);

    private static final String ENTITY_NAME = "crawlserviceCrawlJobState";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrawlJobStateService crawlJobStateService;

    private final CrawlJobStateRepository crawlJobStateRepository;

    public CrawlJobStateResource(CrawlJobStateService crawlJobStateService, CrawlJobStateRepository crawlJobStateRepository) {
        this.crawlJobStateService = crawlJobStateService;
        this.crawlJobStateRepository = crawlJobStateRepository;
    }

    /**
     * {@code POST  /crawl-job-states} : Create a new crawlJobState.
     *
     * @param crawlJobStateDTO the crawlJobStateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crawlJobStateDTO, or with status {@code 400 (Bad Request)} if the crawlJobState has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CrawlJobStateDTO>> createCrawlJobState(@Valid @RequestBody CrawlJobStateDTO crawlJobStateDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CrawlJobState : {}", crawlJobStateDTO);
        if (crawlJobStateDTO.getId() != null) {
            throw new BadRequestAlertException("A new crawlJobState cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return crawlJobStateService
            .save(crawlJobStateDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/crawl-job-states/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /crawl-job-states/:id} : Updates an existing crawlJobState.
     *
     * @param id the id of the crawlJobStateDTO to save.
     * @param crawlJobStateDTO the crawlJobStateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crawlJobStateDTO,
     * or with status {@code 400 (Bad Request)} if the crawlJobStateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crawlJobStateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CrawlJobStateDTO>> updateCrawlJobState(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody CrawlJobStateDTO crawlJobStateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CrawlJobState : {}, {}", id, crawlJobStateDTO);
        if (crawlJobStateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crawlJobStateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return crawlJobStateRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return crawlJobStateService
                    .update(crawlJobStateDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /crawl-job-states/:id} : Partial updates given fields of an existing crawlJobState, field will ignore if it is null
     *
     * @param id the id of the crawlJobStateDTO to save.
     * @param crawlJobStateDTO the crawlJobStateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crawlJobStateDTO,
     * or with status {@code 400 (Bad Request)} if the crawlJobStateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crawlJobStateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crawlJobStateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CrawlJobStateDTO>> partialUpdateCrawlJobState(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody CrawlJobStateDTO crawlJobStateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CrawlJobState partially : {}, {}", id, crawlJobStateDTO);
        if (crawlJobStateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crawlJobStateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return crawlJobStateRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CrawlJobStateDTO> result = crawlJobStateService.partialUpdate(crawlJobStateDTO);

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
     * {@code GET  /crawl-job-states} : get all the crawlJobStates.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crawlJobStates in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<CrawlJobStateDTO>>> getAllCrawlJobStates(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of CrawlJobStates");
        return crawlJobStateService
            .countAll()
            .zipWith(crawlJobStateService.findAll(pageable).collectList())
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
     * {@code GET  /crawl-job-states/:id} : get the "id" crawlJobState.
     *
     * @param id the id of the crawlJobStateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crawlJobStateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CrawlJobStateDTO>> getCrawlJobState(@PathVariable("id") String id) {
        LOG.debug("REST request to get CrawlJobState : {}", id);
        Mono<CrawlJobStateDTO> crawlJobStateDTO = crawlJobStateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crawlJobStateDTO);
    }

    /**
     * {@code DELETE  /crawl-job-states/:id} : delete the "id" crawlJobState.
     *
     * @param id the id of the crawlJobStateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCrawlJobState(@PathVariable("id") String id) {
        LOG.debug("REST request to delete CrawlJobState : {}", id);
        return crawlJobStateService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
