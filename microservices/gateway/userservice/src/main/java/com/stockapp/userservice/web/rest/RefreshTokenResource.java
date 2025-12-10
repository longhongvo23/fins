package com.stockapp.userservice.web.rest;

import com.stockapp.userservice.repository.RefreshTokenRepository;
import com.stockapp.userservice.service.RefreshTokenService;
import com.stockapp.userservice.service.dto.RefreshTokenDTO;
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
 * REST controller for managing {@link com.stockapp.userservice.domain.RefreshToken}.
 */
@RestController
@RequestMapping("/api/refresh-tokens")
public class RefreshTokenResource {

    private static final Logger LOG = LoggerFactory.getLogger(RefreshTokenResource.class);

    private static final String ENTITY_NAME = "userserviceRefreshToken";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RefreshTokenService refreshTokenService;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenResource(RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * {@code POST  /refresh-tokens} : Create a new refreshToken.
     *
     * @param refreshTokenDTO the refreshTokenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new refreshTokenDTO, or with status {@code 400 (Bad Request)} if the refreshToken has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<RefreshTokenDTO>> createRefreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save RefreshToken : {}", refreshTokenDTO);
        if (refreshTokenDTO.getId() != null) {
            throw new BadRequestAlertException("A new refreshToken cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return refreshTokenService
            .save(refreshTokenDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/refresh-tokens/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /refresh-tokens/:id} : Updates an existing refreshToken.
     *
     * @param id the id of the refreshTokenDTO to save.
     * @param refreshTokenDTO the refreshTokenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refreshTokenDTO,
     * or with status {@code 400 (Bad Request)} if the refreshTokenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the refreshTokenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<RefreshTokenDTO>> updateRefreshToken(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody RefreshTokenDTO refreshTokenDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RefreshToken : {}, {}", id, refreshTokenDTO);
        if (refreshTokenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refreshTokenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return refreshTokenRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return refreshTokenService
                    .update(refreshTokenDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /refresh-tokens/:id} : Partial updates given fields of an existing refreshToken, field will ignore if it is null
     *
     * @param id the id of the refreshTokenDTO to save.
     * @param refreshTokenDTO the refreshTokenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refreshTokenDTO,
     * or with status {@code 400 (Bad Request)} if the refreshTokenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the refreshTokenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the refreshTokenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RefreshTokenDTO>> partialUpdateRefreshToken(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody RefreshTokenDTO refreshTokenDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RefreshToken partially : {}, {}", id, refreshTokenDTO);
        if (refreshTokenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refreshTokenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return refreshTokenRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RefreshTokenDTO> result = refreshTokenService.partialUpdate(refreshTokenDTO);

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
     * {@code GET  /refresh-tokens} : get all the refreshTokens.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of refreshTokens in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<RefreshTokenDTO>>> getAllRefreshTokens(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of RefreshTokens");
        return refreshTokenService
            .countAll()
            .zipWith(refreshTokenService.findAll(pageable).collectList())
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
     * {@code GET  /refresh-tokens/:id} : get the "id" refreshToken.
     *
     * @param id the id of the refreshTokenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the refreshTokenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<RefreshTokenDTO>> getRefreshToken(@PathVariable("id") String id) {
        LOG.debug("REST request to get RefreshToken : {}", id);
        Mono<RefreshTokenDTO> refreshTokenDTO = refreshTokenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(refreshTokenDTO);
    }

    /**
     * {@code DELETE  /refresh-tokens/:id} : delete the "id" refreshToken.
     *
     * @param id the id of the refreshTokenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRefreshToken(@PathVariable("id") String id) {
        LOG.debug("REST request to delete RefreshToken : {}", id);
        return refreshTokenService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
