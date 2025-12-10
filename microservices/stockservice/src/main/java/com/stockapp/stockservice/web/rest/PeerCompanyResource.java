package com.stockapp.stockservice.web.rest;

import com.stockapp.stockservice.repository.PeerCompanyRepository;
import com.stockapp.stockservice.service.PeerCompanyService;
import com.stockapp.stockservice.service.dto.PeerCompanyDTO;
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
 * REST controller for managing {@link com.stockapp.stockservice.domain.PeerCompany}.
 */
@RestController
@RequestMapping("/api/peer-companies")
public class PeerCompanyResource {

    private static final Logger LOG = LoggerFactory.getLogger(PeerCompanyResource.class);

    private static final String ENTITY_NAME = "stockservicePeerCompany";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeerCompanyService peerCompanyService;

    private final PeerCompanyRepository peerCompanyRepository;

    public PeerCompanyResource(PeerCompanyService peerCompanyService, PeerCompanyRepository peerCompanyRepository) {
        this.peerCompanyService = peerCompanyService;
        this.peerCompanyRepository = peerCompanyRepository;
    }

    /**
     * {@code POST  /peer-companies} : Create a new peerCompany.
     *
     * @param peerCompanyDTO the peerCompanyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new peerCompanyDTO, or with status {@code 400 (Bad Request)} if the peerCompany has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PeerCompanyDTO>> createPeerCompany(@Valid @RequestBody PeerCompanyDTO peerCompanyDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PeerCompany : {}", peerCompanyDTO);
        if (peerCompanyDTO.getId() != null) {
            throw new BadRequestAlertException("A new peerCompany cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return peerCompanyService
            .save(peerCompanyDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/peer-companies/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /peer-companies/:id} : Updates an existing peerCompany.
     *
     * @param id the id of the peerCompanyDTO to save.
     * @param peerCompanyDTO the peerCompanyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peerCompanyDTO,
     * or with status {@code 400 (Bad Request)} if the peerCompanyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the peerCompanyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PeerCompanyDTO>> updatePeerCompany(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody PeerCompanyDTO peerCompanyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PeerCompany : {}, {}", id, peerCompanyDTO);
        if (peerCompanyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peerCompanyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return peerCompanyRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return peerCompanyService
                    .update(peerCompanyDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /peer-companies/:id} : Partial updates given fields of an existing peerCompany, field will ignore if it is null
     *
     * @param id the id of the peerCompanyDTO to save.
     * @param peerCompanyDTO the peerCompanyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peerCompanyDTO,
     * or with status {@code 400 (Bad Request)} if the peerCompanyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the peerCompanyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the peerCompanyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PeerCompanyDTO>> partialUpdatePeerCompany(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody PeerCompanyDTO peerCompanyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PeerCompany partially : {}, {}", id, peerCompanyDTO);
        if (peerCompanyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peerCompanyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return peerCompanyRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PeerCompanyDTO> result = peerCompanyService.partialUpdate(peerCompanyDTO);

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
     * {@code GET  /peer-companies} : get all the peerCompanies.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of peerCompanies in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PeerCompanyDTO>>> getAllPeerCompanies(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of PeerCompanies");
        return peerCompanyService
            .countAll()
            .zipWith(peerCompanyService.findAll(pageable).collectList())
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
     * {@code GET  /peer-companies/:id} : get the "id" peerCompany.
     *
     * @param id the id of the peerCompanyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the peerCompanyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PeerCompanyDTO>> getPeerCompany(@PathVariable("id") String id) {
        LOG.debug("REST request to get PeerCompany : {}", id);
        Mono<PeerCompanyDTO> peerCompanyDTO = peerCompanyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(peerCompanyDTO);
    }

    /**
     * {@code DELETE  /peer-companies/:id} : delete the "id" peerCompany.
     *
     * @param id the id of the peerCompanyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePeerCompany(@PathVariable("id") String id) {
        LOG.debug("REST request to delete PeerCompany : {}", id);
        return peerCompanyService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
