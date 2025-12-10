package com.stockapp.stockservice.web.rest;

import com.stockapp.stockservice.repository.HistoricalPriceRepository;
import com.stockapp.stockservice.service.HistoricalPriceService;
import com.stockapp.stockservice.service.dto.HistoricalPriceDTO;
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
 * REST controller for managing
 * {@link com.stockapp.stockservice.domain.HistoricalPrice}.
 */
@RestController
@RequestMapping("/api/historical-prices")
public class HistoricalPriceResource {

    private static final Logger LOG = LoggerFactory.getLogger(HistoricalPriceResource.class);

    private static final String ENTITY_NAME = "stockserviceHistoricalPrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoricalPriceService historicalPriceService;

    private final HistoricalPriceRepository historicalPriceRepository;

    public HistoricalPriceResource(HistoricalPriceService historicalPriceService,
            HistoricalPriceRepository historicalPriceRepository) {
        this.historicalPriceService = historicalPriceService;
        this.historicalPriceRepository = historicalPriceRepository;
    }

    /**
     * {@code POST  /historical-prices} : Create a new historicalPrice.
     *
     * @param historicalPriceDTO the historicalPriceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new historicalPriceDTO, or with status
     *         {@code 400 (Bad Request)} if the historicalPrice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<HistoricalPriceDTO>> createHistoricalPrice(
            @Valid @RequestBody HistoricalPriceDTO historicalPriceDTO)
            throws URISyntaxException {
        LOG.debug("REST request to save HistoricalPrice : {}", historicalPriceDTO);
        if (historicalPriceDTO.getId() != null) {
            throw new BadRequestAlertException("A new historicalPrice cannot already have an ID", ENTITY_NAME,
                    "idexists");
        }
        return historicalPriceService
                .save(historicalPriceDTO)
                .map(result -> {
                    try {
                        return ResponseEntity.created(new URI("/api/historical-prices/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                                        result.getId()))
                                .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * {@code PUT  /historical-prices/:id} : Updates an existing historicalPrice.
     *
     * @param id                 the id of the historicalPriceDTO to save.
     * @param historicalPriceDTO the historicalPriceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated historicalPriceDTO,
     *         or with status {@code 400 (Bad Request)} if the historicalPriceDTO is
     *         not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         historicalPriceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<HistoricalPriceDTO>> updateHistoricalPrice(
            @PathVariable(value = "id", required = false) final String id,
            @Valid @RequestBody HistoricalPriceDTO historicalPriceDTO) throws URISyntaxException {
        LOG.debug("REST request to update HistoricalPrice : {}, {}", id, historicalPriceDTO);
        if (historicalPriceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historicalPriceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return historicalPriceRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return historicalPriceService
                            .update(historicalPriceDTO)
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                            .map(result -> ResponseEntity.ok()
                                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                                            result.getId()))
                                    .body(result));
                });
    }

    /**
     * {@code PATCH  /historical-prices/:id} : Partial updates given fields of an
     * existing historicalPrice, field will ignore if it is null
     *
     * @param id                 the id of the historicalPriceDTO to save.
     * @param historicalPriceDTO the historicalPriceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated historicalPriceDTO,
     *         or with status {@code 400 (Bad Request)} if the historicalPriceDTO is
     *         not valid,
     *         or with status {@code 404 (Not Found)} if the historicalPriceDTO is
     *         not found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         historicalPriceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HistoricalPriceDTO>> partialUpdateHistoricalPrice(
            @PathVariable(value = "id", required = false) final String id,
            @NotNull @RequestBody HistoricalPriceDTO historicalPriceDTO) throws URISyntaxException {
        LOG.debug("REST request to partial update HistoricalPrice partially : {}, {}", id, historicalPriceDTO);
        if (historicalPriceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historicalPriceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return historicalPriceRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<HistoricalPriceDTO> result = historicalPriceService.partialUpdate(historicalPriceDTO);

                    return result
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                            .map(res -> ResponseEntity.ok()
                                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                                            res.getId()))
                                    .body(res));
                });
    }

    /**
     * {@code GET  /historical-prices} : get all the historicalPrices.
     *
     * @param pageable  the pagination information.
     * @param request   a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is
     *                  applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of historicalPrices in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<HistoricalPriceDTO>>> getAllHistoricalPrices(
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request,
            @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get a page of HistoricalPrices");
        return historicalPriceService
                .countAll()
                .zipWith(historicalPriceService.findAll(pageable).collectList())
                .map(countWithEntities -> ResponseEntity.ok()
                        .headers(
                                PaginationUtil.generatePaginationHttpHeaders(
                                        ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(),
                                                request.getHeaders()),
                                        new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())))
                        .body(countWithEntities.getT2()));
    }

    /**
     * {@code GET  /historical-prices/by-symbol/:symbol} : get historical prices by
     * symbol.
     *
     * @param symbol   the symbol to filter by.
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of historicalPrices for the symbol.
     */
    @GetMapping(value = "/by-symbol/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<HistoricalPriceDTO>>> getHistoricalPricesBySymbol(
            @PathVariable("symbol") String symbol,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request) {
        LOG.debug("REST request to get HistoricalPrices for symbol: {}", symbol);
        return historicalPriceRepository
                .countBySymbol(symbol)
                .zipWith(historicalPriceService.findBySymbol(symbol, pageable).collectList())
                .map(countWithEntities -> ResponseEntity.ok()
                        .headers(
                                PaginationUtil.generatePaginationHttpHeaders(
                                        ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(),
                                                request.getHeaders()),
                                        new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())))
                        .body(countWithEntities.getT2()));
    }

    /**
     * {@code GET  /historical-prices/:id} : get the "id" historicalPrice.
     *
     * @param id the id of the historicalPriceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the historicalPriceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<HistoricalPriceDTO>> getHistoricalPrice(@PathVariable("id") String id) {
        LOG.debug("REST request to get HistoricalPrice : {}", id);
        Mono<HistoricalPriceDTO> historicalPriceDTO = historicalPriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historicalPriceDTO);
    }

    /**
     * {@code DELETE  /historical-prices/:id} : delete the "id" historicalPrice.
     *
     * @param id the id of the historicalPriceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteHistoricalPrice(@PathVariable("id") String id) {
        LOG.debug("REST request to delete HistoricalPrice : {}", id);
        return historicalPriceService
                .delete(id)
                .then(
                        Mono.just(
                                ResponseEntity.noContent().headers(
                                        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id))
                                        .build()));
    }
}
