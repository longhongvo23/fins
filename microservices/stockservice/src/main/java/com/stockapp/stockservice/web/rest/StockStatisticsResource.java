package com.stockapp.stockservice.web.rest;

import com.stockapp.stockservice.repository.StockStatisticsRepository;
import com.stockapp.stockservice.service.StockStatisticsService;
import com.stockapp.stockservice.service.dto.StockStatisticsDTO;
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
 * REST controller for managing {@link com.stockapp.stockservice.domain.StockStatistics}.
 */
@RestController
@RequestMapping("/api/stock-statistics")
public class StockStatisticsResource {

    private static final Logger LOG = LoggerFactory.getLogger(StockStatisticsResource.class);

    private static final String ENTITY_NAME = "stockserviceStockStatistics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockStatisticsService stockStatisticsService;

    private final StockStatisticsRepository stockStatisticsRepository;

    public StockStatisticsResource(StockStatisticsService stockStatisticsService, StockStatisticsRepository stockStatisticsRepository) {
        this.stockStatisticsService = stockStatisticsService;
        this.stockStatisticsRepository = stockStatisticsRepository;
    }

    /**
     * {@code POST  /stock-statistics} : Create a new stockStatistics.
     *
     * @param stockStatisticsDTO the stockStatisticsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockStatisticsDTO, or with status {@code 400 (Bad Request)} if the stockStatistics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<StockStatisticsDTO>> createStockStatistics(@Valid @RequestBody StockStatisticsDTO stockStatisticsDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save StockStatistics : {}", stockStatisticsDTO);
        if (stockStatisticsDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockStatistics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return stockStatisticsService
            .save(stockStatisticsDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/stock-statistics/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /stock-statistics/:id} : Updates an existing stockStatistics.
     *
     * @param id the id of the stockStatisticsDTO to save.
     * @param stockStatisticsDTO the stockStatisticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockStatisticsDTO,
     * or with status {@code 400 (Bad Request)} if the stockStatisticsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockStatisticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<StockStatisticsDTO>> updateStockStatistics(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody StockStatisticsDTO stockStatisticsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StockStatistics : {}, {}", id, stockStatisticsDTO);
        if (stockStatisticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockStatisticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return stockStatisticsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return stockStatisticsService
                    .update(stockStatisticsDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /stock-statistics/:id} : Partial updates given fields of an existing stockStatistics, field will ignore if it is null
     *
     * @param id the id of the stockStatisticsDTO to save.
     * @param stockStatisticsDTO the stockStatisticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockStatisticsDTO,
     * or with status {@code 400 (Bad Request)} if the stockStatisticsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stockStatisticsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockStatisticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<StockStatisticsDTO>> partialUpdateStockStatistics(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody StockStatisticsDTO stockStatisticsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StockStatistics partially : {}, {}", id, stockStatisticsDTO);
        if (stockStatisticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockStatisticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return stockStatisticsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<StockStatisticsDTO> result = stockStatisticsService.partialUpdate(stockStatisticsDTO);

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
     * {@code GET  /stock-statistics} : get all the stockStatistics.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stockStatistics in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<StockStatisticsDTO>>> getAllStockStatistics(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of StockStatistics");
        return stockStatisticsService
            .countAll()
            .zipWith(stockStatisticsService.findAll(pageable).collectList())
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
     * {@code GET  /stock-statistics/:id} : get the "id" stockStatistics.
     *
     * @param id the id of the stockStatisticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockStatisticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<StockStatisticsDTO>> getStockStatistics(@PathVariable("id") String id) {
        LOG.debug("REST request to get StockStatistics : {}", id);
        Mono<StockStatisticsDTO> stockStatisticsDTO = stockStatisticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockStatisticsDTO);
    }

    /**
     * {@code DELETE  /stock-statistics/:id} : delete the "id" stockStatistics.
     *
     * @param id the id of the stockStatisticsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteStockStatistics(@PathVariable("id") String id) {
        LOG.debug("REST request to delete StockStatistics : {}", id);
        return stockStatisticsService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
