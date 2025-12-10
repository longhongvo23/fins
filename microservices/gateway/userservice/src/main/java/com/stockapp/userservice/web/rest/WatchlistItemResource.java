package com.stockapp.userservice.web.rest;

import com.stockapp.userservice.repository.WatchlistItemRepository;
import com.stockapp.userservice.service.WatchlistItemService;
import com.stockapp.userservice.service.dto.WatchlistItemDTO;
import com.stockapp.userservice.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.stockapp.userservice.domain.WatchlistItem}.
 */
@RestController
@RequestMapping("/api/watchlist-items")
public class WatchlistItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(WatchlistItemResource.class);

    private static final String ENTITY_NAME = "userserviceWatchlistItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WatchlistItemService watchlistItemService;

    private final WatchlistItemRepository watchlistItemRepository;

    public WatchlistItemResource(WatchlistItemService watchlistItemService, WatchlistItemRepository watchlistItemRepository) {
        this.watchlistItemService = watchlistItemService;
        this.watchlistItemRepository = watchlistItemRepository;
    }

    /**
     * {@code POST  /watchlist-items} : Create a new watchlistItem.
     *
     * @param watchlistItemDTO the watchlistItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new watchlistItemDTO, or with status {@code 400 (Bad Request)} if the watchlistItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<WatchlistItemDTO>> createWatchlistItem(@Valid @RequestBody WatchlistItemDTO watchlistItemDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save WatchlistItem : {}", watchlistItemDTO);
        if (watchlistItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new watchlistItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return watchlistItemService
            .save(watchlistItemDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/watchlist-items/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /watchlist-items/:id} : Updates an existing watchlistItem.
     *
     * @param id the id of the watchlistItemDTO to save.
     * @param watchlistItemDTO the watchlistItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated watchlistItemDTO,
     * or with status {@code 400 (Bad Request)} if the watchlistItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the watchlistItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<WatchlistItemDTO>> updateWatchlistItem(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody WatchlistItemDTO watchlistItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update WatchlistItem : {}, {}", id, watchlistItemDTO);
        if (watchlistItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, watchlistItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return watchlistItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return watchlistItemService
                    .update(watchlistItemDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /watchlist-items/:id} : Partial updates given fields of an existing watchlistItem, field will ignore if it is null
     *
     * @param id the id of the watchlistItemDTO to save.
     * @param watchlistItemDTO the watchlistItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated watchlistItemDTO,
     * or with status {@code 400 (Bad Request)} if the watchlistItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the watchlistItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the watchlistItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<WatchlistItemDTO>> partialUpdateWatchlistItem(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody WatchlistItemDTO watchlistItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WatchlistItem partially : {}, {}", id, watchlistItemDTO);
        if (watchlistItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, watchlistItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return watchlistItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WatchlistItemDTO> result = watchlistItemService.partialUpdate(watchlistItemDTO);

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
     * {@code GET  /watchlist-items} : get all the watchlistItems.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of watchlistItems in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<WatchlistItemDTO>>> getAllWatchlistItems(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of WatchlistItems");
        return watchlistItemService
            .countAll()
            .zipWith(watchlistItemService.findAll(pageable).collectList())
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
     * {@code GET  /watchlist-items/:id} : get the "id" watchlistItem.
     *
     * @param id the id of the watchlistItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the watchlistItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<WatchlistItemDTO>> getWatchlistItem(@PathVariable("id") String id) {
        LOG.debug("REST request to get WatchlistItem : {}", id);
        Mono<WatchlistItemDTO> watchlistItemDTO = watchlistItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(watchlistItemDTO);
    }

    /**
     * {@code DELETE  /watchlist-items/:id} : delete the "id" watchlistItem.
     *
     * @param id the id of the watchlistItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteWatchlistItem(@PathVariable("id") String id) {
        LOG.debug("REST request to delete WatchlistItem : {}", id);
        return watchlistItemService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
