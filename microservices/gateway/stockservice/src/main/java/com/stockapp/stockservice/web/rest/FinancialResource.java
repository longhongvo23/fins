package com.stockapp.stockservice.web.rest;

import com.stockapp.stockservice.repository.FinancialRepository;
import com.stockapp.stockservice.service.FinancialService;
import com.stockapp.stockservice.service.dto.FinancialDTO;
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
 * REST controller for managing {@link com.stockapp.stockservice.domain.Financial}.
 */
@RestController
@RequestMapping("/api/financials")
public class FinancialResource {

    private static final Logger LOG = LoggerFactory.getLogger(FinancialResource.class);

    private static final String ENTITY_NAME = "stockserviceFinancial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinancialService financialService;

    private final FinancialRepository financialRepository;

    public FinancialResource(FinancialService financialService, FinancialRepository financialRepository) {
        this.financialService = financialService;
        this.financialRepository = financialRepository;
    }

    /**
     * {@code POST  /financials} : Create a new financial.
     *
     * @param financialDTO the financialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new financialDTO, or with status {@code 400 (Bad Request)} if the financial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FinancialDTO>> createFinancial(@Valid @RequestBody FinancialDTO financialDTO) throws URISyntaxException {
        LOG.debug("REST request to save Financial : {}", financialDTO);
        if (financialDTO.getId() != null) {
            throw new BadRequestAlertException("A new financial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return financialService
            .save(financialDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/financials/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /financials/:id} : Updates an existing financial.
     *
     * @param id the id of the financialDTO to save.
     * @param financialDTO the financialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialDTO,
     * or with status {@code 400 (Bad Request)} if the financialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the financialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FinancialDTO>> updateFinancial(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody FinancialDTO financialDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Financial : {}, {}", id, financialDTO);
        if (financialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return financialRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return financialService
                    .update(financialDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /financials/:id} : Partial updates given fields of an existing financial, field will ignore if it is null
     *
     * @param id the id of the financialDTO to save.
     * @param financialDTO the financialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialDTO,
     * or with status {@code 400 (Bad Request)} if the financialDTO is not valid,
     * or with status {@code 404 (Not Found)} if the financialDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the financialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FinancialDTO>> partialUpdateFinancial(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody FinancialDTO financialDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Financial partially : {}, {}", id, financialDTO);
        if (financialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return financialRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FinancialDTO> result = financialService.partialUpdate(financialDTO);

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
     * {@code GET  /financials} : get all the financials.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of financials in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<FinancialDTO>>> getAllFinancials(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Financials");
        return financialService
            .countAll()
            .zipWith(financialService.findAll(pageable).collectList())
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
     * {@code GET  /financials/:id} : get the "id" financial.
     *
     * @param id the id of the financialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the financialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FinancialDTO>> getFinancial(@PathVariable("id") String id) {
        LOG.debug("REST request to get Financial : {}", id);
        Mono<FinancialDTO> financialDTO = financialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(financialDTO);
    }

    /**
     * {@code DELETE  /financials/:id} : delete the "id" financial.
     *
     * @param id the id of the financialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFinancial(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Financial : {}", id);
        return financialService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
