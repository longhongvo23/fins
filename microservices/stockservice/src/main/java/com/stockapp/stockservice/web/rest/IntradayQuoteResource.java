package com.stockapp.stockservice.web.rest;

import com.stockapp.stockservice.repository.IntradayQuoteRepository;
import com.stockapp.stockservice.service.IntradayQuoteService;
import com.stockapp.stockservice.service.dto.IntradayQuoteDTO;
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
 * REST controller for managing {@link com.stockapp.stockservice.domain.IntradayQuote}.
 */
@RestController
@RequestMapping("/api/intraday-quotes")
public class IntradayQuoteResource {

    private static final Logger LOG = LoggerFactory.getLogger(IntradayQuoteResource.class);

    private static final String ENTITY_NAME = "stockserviceIntradayQuote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IntradayQuoteService intradayQuoteService;

    private final IntradayQuoteRepository intradayQuoteRepository;

    public IntradayQuoteResource(IntradayQuoteService intradayQuoteService, IntradayQuoteRepository intradayQuoteRepository) {
        this.intradayQuoteService = intradayQuoteService;
        this.intradayQuoteRepository = intradayQuoteRepository;
    }

    /**
     * {@code POST  /intraday-quotes} : Create a new intradayQuote.
     *
     * @param intradayQuoteDTO the intradayQuoteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new intradayQuoteDTO, or with status {@code 400 (Bad Request)} if the intradayQuote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<IntradayQuoteDTO>> createIntradayQuote(@Valid @RequestBody IntradayQuoteDTO intradayQuoteDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save IntradayQuote : {}", intradayQuoteDTO);
        if (intradayQuoteDTO.getId() != null) {
            throw new BadRequestAlertException("A new intradayQuote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return intradayQuoteService
            .save(intradayQuoteDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/intraday-quotes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /intraday-quotes/:id} : Updates an existing intradayQuote.
     *
     * @param id the id of the intradayQuoteDTO to save.
     * @param intradayQuoteDTO the intradayQuoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intradayQuoteDTO,
     * or with status {@code 400 (Bad Request)} if the intradayQuoteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the intradayQuoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<IntradayQuoteDTO>> updateIntradayQuote(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody IntradayQuoteDTO intradayQuoteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update IntradayQuote : {}, {}", id, intradayQuoteDTO);
        if (intradayQuoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intradayQuoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return intradayQuoteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return intradayQuoteService
                    .update(intradayQuoteDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /intraday-quotes/:id} : Partial updates given fields of an existing intradayQuote, field will ignore if it is null
     *
     * @param id the id of the intradayQuoteDTO to save.
     * @param intradayQuoteDTO the intradayQuoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intradayQuoteDTO,
     * or with status {@code 400 (Bad Request)} if the intradayQuoteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the intradayQuoteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the intradayQuoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IntradayQuoteDTO>> partialUpdateIntradayQuote(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody IntradayQuoteDTO intradayQuoteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IntradayQuote partially : {}, {}", id, intradayQuoteDTO);
        if (intradayQuoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intradayQuoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return intradayQuoteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IntradayQuoteDTO> result = intradayQuoteService.partialUpdate(intradayQuoteDTO);

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
     * {@code GET  /intraday-quotes} : get all the intradayQuotes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of intradayQuotes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<IntradayQuoteDTO>>> getAllIntradayQuotes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of IntradayQuotes");
        return intradayQuoteService
            .countAll()
            .zipWith(intradayQuoteService.findAll(pageable).collectList())
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
     * {@code GET  /intraday-quotes/:id} : get the "id" intradayQuote.
     *
     * @param id the id of the intradayQuoteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the intradayQuoteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<IntradayQuoteDTO>> getIntradayQuote(@PathVariable("id") String id) {
        LOG.debug("REST request to get IntradayQuote : {}", id);
        Mono<IntradayQuoteDTO> intradayQuoteDTO = intradayQuoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(intradayQuoteDTO);
    }

    /**
     * {@code DELETE  /intraday-quotes/:id} : delete the "id" intradayQuote.
     *
     * @param id the id of the intradayQuoteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteIntradayQuote(@PathVariable("id") String id) {
        LOG.debug("REST request to delete IntradayQuote : {}", id);
        return intradayQuoteService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
