package com.stockapp.newsservice.web.rest;

import com.stockapp.newsservice.repository.CompanyNewsRepository;
import com.stockapp.newsservice.service.CompanyNewsService;
import com.stockapp.newsservice.service.dto.CompanyNewsDTO;
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
 * REST controller for managing {@link com.stockapp.newsservice.domain.CompanyNews}.
 */
@RestController
@RequestMapping("/api/company-news")
public class CompanyNewsResource {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyNewsResource.class);

    private static final String ENTITY_NAME = "newsserviceCompanyNews";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyNewsService companyNewsService;

    private final CompanyNewsRepository companyNewsRepository;

    public CompanyNewsResource(CompanyNewsService companyNewsService, CompanyNewsRepository companyNewsRepository) {
        this.companyNewsService = companyNewsService;
        this.companyNewsRepository = companyNewsRepository;
    }

    /**
     * {@code POST  /company-news} : Create a new companyNews.
     *
     * @param companyNewsDTO the companyNewsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyNewsDTO, or with status {@code 400 (Bad Request)} if the companyNews has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CompanyNewsDTO>> createCompanyNews(@Valid @RequestBody CompanyNewsDTO companyNewsDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CompanyNews : {}", companyNewsDTO);
        if (companyNewsDTO.getId() != null) {
            throw new BadRequestAlertException("A new companyNews cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return companyNewsService
            .save(companyNewsDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/company-news/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /company-news/:id} : Updates an existing companyNews.
     *
     * @param id the id of the companyNewsDTO to save.
     * @param companyNewsDTO the companyNewsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyNewsDTO,
     * or with status {@code 400 (Bad Request)} if the companyNewsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyNewsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CompanyNewsDTO>> updateCompanyNews(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody CompanyNewsDTO companyNewsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CompanyNews : {}, {}", id, companyNewsDTO);
        if (companyNewsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyNewsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return companyNewsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return companyNewsService
                    .update(companyNewsDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /company-news/:id} : Partial updates given fields of an existing companyNews, field will ignore if it is null
     *
     * @param id the id of the companyNewsDTO to save.
     * @param companyNewsDTO the companyNewsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyNewsDTO,
     * or with status {@code 400 (Bad Request)} if the companyNewsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the companyNewsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyNewsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CompanyNewsDTO>> partialUpdateCompanyNews(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody CompanyNewsDTO companyNewsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CompanyNews partially : {}, {}", id, companyNewsDTO);
        if (companyNewsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyNewsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return companyNewsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CompanyNewsDTO> result = companyNewsService.partialUpdate(companyNewsDTO);

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
     * {@code GET  /company-news} : get all the companyNews.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyNews in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<CompanyNewsDTO>>> getAllCompanyNews(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of CompanyNews");
        return companyNewsService
            .countAll()
            .zipWith(companyNewsService.findAll(pageable).collectList())
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
     * {@code GET  /company-news/:id} : get the "id" companyNews.
     *
     * @param id the id of the companyNewsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyNewsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CompanyNewsDTO>> getCompanyNews(@PathVariable("id") String id) {
        LOG.debug("REST request to get CompanyNews : {}", id);
        Mono<CompanyNewsDTO> companyNewsDTO = companyNewsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyNewsDTO);
    }

    /**
     * {@code DELETE  /company-news/:id} : delete the "id" companyNews.
     *
     * @param id the id of the companyNewsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCompanyNews(@PathVariable("id") String id) {
        LOG.debug("REST request to delete CompanyNews : {}", id);
        return companyNewsService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
