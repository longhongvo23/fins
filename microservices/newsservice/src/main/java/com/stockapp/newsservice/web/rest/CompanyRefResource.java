package com.stockapp.newsservice.web.rest;

import com.stockapp.newsservice.repository.CompanyRefRepository;
import com.stockapp.newsservice.service.CompanyRefService;
import com.stockapp.newsservice.service.dto.CompanyRefDTO;
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
 * REST controller for managing {@link com.stockapp.newsservice.domain.CompanyRef}.
 */
@RestController
@RequestMapping("/api/company-refs")
public class CompanyRefResource {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyRefResource.class);

    private static final String ENTITY_NAME = "newsserviceCompanyRef";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyRefService companyRefService;

    private final CompanyRefRepository companyRefRepository;

    public CompanyRefResource(CompanyRefService companyRefService, CompanyRefRepository companyRefRepository) {
        this.companyRefService = companyRefService;
        this.companyRefRepository = companyRefRepository;
    }

    /**
     * {@code POST  /company-refs} : Create a new companyRef.
     *
     * @param companyRefDTO the companyRefDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyRefDTO, or with status {@code 400 (Bad Request)} if the companyRef has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CompanyRefDTO>> createCompanyRef(@Valid @RequestBody CompanyRefDTO companyRefDTO) throws URISyntaxException {
        LOG.debug("REST request to save CompanyRef : {}", companyRefDTO);
        if (companyRefDTO.getId() != null) {
            throw new BadRequestAlertException("A new companyRef cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return companyRefService
            .save(companyRefDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/company-refs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /company-refs/:id} : Updates an existing companyRef.
     *
     * @param id the id of the companyRefDTO to save.
     * @param companyRefDTO the companyRefDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyRefDTO,
     * or with status {@code 400 (Bad Request)} if the companyRefDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyRefDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CompanyRefDTO>> updateCompanyRef(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody CompanyRefDTO companyRefDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CompanyRef : {}, {}", id, companyRefDTO);
        if (companyRefDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyRefDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return companyRefRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return companyRefService
                    .update(companyRefDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /company-refs/:id} : Partial updates given fields of an existing companyRef, field will ignore if it is null
     *
     * @param id the id of the companyRefDTO to save.
     * @param companyRefDTO the companyRefDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyRefDTO,
     * or with status {@code 400 (Bad Request)} if the companyRefDTO is not valid,
     * or with status {@code 404 (Not Found)} if the companyRefDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyRefDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CompanyRefDTO>> partialUpdateCompanyRef(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody CompanyRefDTO companyRefDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CompanyRef partially : {}, {}", id, companyRefDTO);
        if (companyRefDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyRefDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return companyRefRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CompanyRefDTO> result = companyRefService.partialUpdate(companyRefDTO);

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
     * {@code GET  /company-refs} : get all the companyRefs.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyRefs in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<CompanyRefDTO>>> getAllCompanyRefs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of CompanyRefs");
        return companyRefService
            .countAll()
            .zipWith(companyRefService.findAll(pageable).collectList())
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
     * {@code GET  /company-refs/:id} : get the "id" companyRef.
     *
     * @param id the id of the companyRefDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyRefDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CompanyRefDTO>> getCompanyRef(@PathVariable("id") String id) {
        LOG.debug("REST request to get CompanyRef : {}", id);
        Mono<CompanyRefDTO> companyRefDTO = companyRefService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyRefDTO);
    }

    /**
     * {@code DELETE  /company-refs/:id} : delete the "id" companyRef.
     *
     * @param id the id of the companyRefDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCompanyRef(@PathVariable("id") String id) {
        LOG.debug("REST request to delete CompanyRef : {}", id);
        return companyRefService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
