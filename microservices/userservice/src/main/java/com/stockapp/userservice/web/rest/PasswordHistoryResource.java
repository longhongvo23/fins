package com.stockapp.userservice.web.rest;

import com.stockapp.userservice.repository.PasswordHistoryRepository;
import com.stockapp.userservice.service.PasswordHistoryService;
import com.stockapp.userservice.service.dto.PasswordHistoryDTO;
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
 * REST controller for managing {@link com.stockapp.userservice.domain.PasswordHistory}.
 */
@RestController
@RequestMapping("/api/password-histories")
public class PasswordHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordHistoryResource.class);

    private static final String ENTITY_NAME = "userservicePasswordHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PasswordHistoryService passwordHistoryService;

    private final PasswordHistoryRepository passwordHistoryRepository;

    public PasswordHistoryResource(PasswordHistoryService passwordHistoryService, PasswordHistoryRepository passwordHistoryRepository) {
        this.passwordHistoryService = passwordHistoryService;
        this.passwordHistoryRepository = passwordHistoryRepository;
    }

    /**
     * {@code POST  /password-histories} : Create a new passwordHistory.
     *
     * @param passwordHistoryDTO the passwordHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passwordHistoryDTO, or with status {@code 400 (Bad Request)} if the passwordHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PasswordHistoryDTO>> createPasswordHistory(@Valid @RequestBody PasswordHistoryDTO passwordHistoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PasswordHistory : {}", passwordHistoryDTO);
        if (passwordHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new passwordHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return passwordHistoryService
            .save(passwordHistoryDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/password-histories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /password-histories/:id} : Updates an existing passwordHistory.
     *
     * @param id the id of the passwordHistoryDTO to save.
     * @param passwordHistoryDTO the passwordHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passwordHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the passwordHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passwordHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PasswordHistoryDTO>> updatePasswordHistory(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody PasswordHistoryDTO passwordHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PasswordHistory : {}, {}", id, passwordHistoryDTO);
        if (passwordHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passwordHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return passwordHistoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return passwordHistoryService
                    .update(passwordHistoryDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /password-histories/:id} : Partial updates given fields of an existing passwordHistory, field will ignore if it is null
     *
     * @param id the id of the passwordHistoryDTO to save.
     * @param passwordHistoryDTO the passwordHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passwordHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the passwordHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the passwordHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the passwordHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PasswordHistoryDTO>> partialUpdatePasswordHistory(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody PasswordHistoryDTO passwordHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PasswordHistory partially : {}, {}", id, passwordHistoryDTO);
        if (passwordHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passwordHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return passwordHistoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PasswordHistoryDTO> result = passwordHistoryService.partialUpdate(passwordHistoryDTO);

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
     * {@code GET  /password-histories} : get all the passwordHistories.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passwordHistories in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PasswordHistoryDTO>>> getAllPasswordHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of PasswordHistories");
        return passwordHistoryService
            .countAll()
            .zipWith(passwordHistoryService.findAll(pageable).collectList())
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
     * {@code GET  /password-histories/:id} : get the "id" passwordHistory.
     *
     * @param id the id of the passwordHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the passwordHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PasswordHistoryDTO>> getPasswordHistory(@PathVariable("id") String id) {
        LOG.debug("REST request to get PasswordHistory : {}", id);
        Mono<PasswordHistoryDTO> passwordHistoryDTO = passwordHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(passwordHistoryDTO);
    }

    /**
     * {@code DELETE  /password-histories/:id} : delete the "id" passwordHistory.
     *
     * @param id the id of the passwordHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePasswordHistory(@PathVariable("id") String id) {
        LOG.debug("REST request to delete PasswordHistory : {}", id);
        return passwordHistoryService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
