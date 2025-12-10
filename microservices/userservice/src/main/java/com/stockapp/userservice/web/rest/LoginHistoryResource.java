package com.stockapp.userservice.web.rest;

import com.stockapp.userservice.repository.LoginHistoryRepository;
import com.stockapp.userservice.service.LoginHistoryService;
import com.stockapp.userservice.service.dto.LoginHistoryDTO;
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
 * REST controller for managing {@link com.stockapp.userservice.domain.LoginHistory}.
 */
@RestController
@RequestMapping("/api/login-histories")
public class LoginHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(LoginHistoryResource.class);

    private static final String ENTITY_NAME = "userserviceLoginHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoginHistoryService loginHistoryService;

    private final LoginHistoryRepository loginHistoryRepository;

    public LoginHistoryResource(LoginHistoryService loginHistoryService, LoginHistoryRepository loginHistoryRepository) {
        this.loginHistoryService = loginHistoryService;
        this.loginHistoryRepository = loginHistoryRepository;
    }

    /**
     * {@code POST  /login-histories} : Create a new loginHistory.
     *
     * @param loginHistoryDTO the loginHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loginHistoryDTO, or with status {@code 400 (Bad Request)} if the loginHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<LoginHistoryDTO>> createLoginHistory(@Valid @RequestBody LoginHistoryDTO loginHistoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LoginHistory : {}", loginHistoryDTO);
        if (loginHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new loginHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return loginHistoryService
            .save(loginHistoryDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/login-histories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /login-histories/:id} : Updates an existing loginHistory.
     *
     * @param id the id of the loginHistoryDTO to save.
     * @param loginHistoryDTO the loginHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loginHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the loginHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loginHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<LoginHistoryDTO>> updateLoginHistory(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody LoginHistoryDTO loginHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LoginHistory : {}, {}", id, loginHistoryDTO);
        if (loginHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loginHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return loginHistoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return loginHistoryService
                    .update(loginHistoryDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /login-histories/:id} : Partial updates given fields of an existing loginHistory, field will ignore if it is null
     *
     * @param id the id of the loginHistoryDTO to save.
     * @param loginHistoryDTO the loginHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loginHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the loginHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loginHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loginHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<LoginHistoryDTO>> partialUpdateLoginHistory(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody LoginHistoryDTO loginHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LoginHistory partially : {}, {}", id, loginHistoryDTO);
        if (loginHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loginHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return loginHistoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<LoginHistoryDTO> result = loginHistoryService.partialUpdate(loginHistoryDTO);

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
     * {@code GET  /login-histories} : get all the loginHistories.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loginHistories in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<LoginHistoryDTO>>> getAllLoginHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of LoginHistories");
        return loginHistoryService
            .countAll()
            .zipWith(loginHistoryService.findAll(pageable).collectList())
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
     * {@code GET  /login-histories/:id} : get the "id" loginHistory.
     *
     * @param id the id of the loginHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loginHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<LoginHistoryDTO>> getLoginHistory(@PathVariable("id") String id) {
        LOG.debug("REST request to get LoginHistory : {}", id);
        Mono<LoginHistoryDTO> loginHistoryDTO = loginHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loginHistoryDTO);
    }

    /**
     * {@code DELETE  /login-histories/:id} : delete the "id" loginHistory.
     *
     * @param id the id of the loginHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteLoginHistory(@PathVariable("id") String id) {
        LOG.debug("REST request to delete LoginHistory : {}", id);
        return loginHistoryService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
