package com.stockapp.userservice.web.rest;

import com.stockapp.userservice.repository.SessionRepository;
import com.stockapp.userservice.service.SessionService;
import com.stockapp.userservice.service.dto.SessionDTO;
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
 * REST controller for managing {@link com.stockapp.userservice.domain.Session}.
 */
@RestController
@RequestMapping("/api/sessions")
public class SessionResource {

    private static final Logger LOG = LoggerFactory.getLogger(SessionResource.class);

    private static final String ENTITY_NAME = "userserviceSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionService sessionService;

    private final SessionRepository sessionRepository;

    public SessionResource(SessionService sessionService, SessionRepository sessionRepository) {
        this.sessionService = sessionService;
        this.sessionRepository = sessionRepository;
    }

    /**
     * {@code POST  /sessions} : Create a new session.
     *
     * @param sessionDTO the sessionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionDTO, or with status {@code 400 (Bad Request)} if the session has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SessionDTO>> createSession(@Valid @RequestBody SessionDTO sessionDTO) throws URISyntaxException {
        LOG.debug("REST request to save Session : {}", sessionDTO);
        if (sessionDTO.getId() != null) {
            throw new BadRequestAlertException("A new session cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return sessionService
            .save(sessionDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/sessions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /sessions/:id} : Updates an existing session.
     *
     * @param id the id of the sessionDTO to save.
     * @param sessionDTO the sessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionDTO,
     * or with status {@code 400 (Bad Request)} if the sessionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SessionDTO>> updateSession(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody SessionDTO sessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Session : {}, {}", id, sessionDTO);
        if (sessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sessionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return sessionService
                    .update(sessionDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /sessions/:id} : Partial updates given fields of an existing session, field will ignore if it is null
     *
     * @param id the id of the sessionDTO to save.
     * @param sessionDTO the sessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionDTO,
     * or with status {@code 400 (Bad Request)} if the sessionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sessionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SessionDTO>> partialUpdateSession(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody SessionDTO sessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Session partially : {}, {}", id, sessionDTO);
        if (sessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sessionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SessionDTO> result = sessionService.partialUpdate(sessionDTO);

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
     * {@code GET  /sessions} : get all the sessions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessions in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SessionDTO>>> getAllSessions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Sessions");
        return sessionService
            .countAll()
            .zipWith(sessionService.findAll(pageable).collectList())
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
     * {@code GET  /sessions/:id} : get the "id" session.
     *
     * @param id the id of the sessionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SessionDTO>> getSession(@PathVariable("id") String id) {
        LOG.debug("REST request to get Session : {}", id);
        Mono<SessionDTO> sessionDTO = sessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sessionDTO);
    }

    /**
     * {@code DELETE  /sessions/:id} : delete the "id" session.
     *
     * @param id the id of the sessionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSession(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Session : {}", id);
        return sessionService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
