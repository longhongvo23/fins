package com.stockapp.userservice.web.rest;

import com.stockapp.userservice.repository.NotificationSettingRepository;
import com.stockapp.userservice.service.NotificationSettingService;
import com.stockapp.userservice.service.dto.NotificationSettingDTO;
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
 * REST controller for managing {@link com.stockapp.userservice.domain.NotificationSetting}.
 */
@RestController
@RequestMapping("/api/notification-settings")
public class NotificationSettingResource {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationSettingResource.class);

    private static final String ENTITY_NAME = "userserviceNotificationSetting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationSettingService notificationSettingService;

    private final NotificationSettingRepository notificationSettingRepository;

    public NotificationSettingResource(
        NotificationSettingService notificationSettingService,
        NotificationSettingRepository notificationSettingRepository
    ) {
        this.notificationSettingService = notificationSettingService;
        this.notificationSettingRepository = notificationSettingRepository;
    }

    /**
     * {@code POST  /notification-settings} : Create a new notificationSetting.
     *
     * @param notificationSettingDTO the notificationSettingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationSettingDTO, or with status {@code 400 (Bad Request)} if the notificationSetting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<NotificationSettingDTO>> createNotificationSetting(
        @Valid @RequestBody NotificationSettingDTO notificationSettingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save NotificationSetting : {}", notificationSettingDTO);
        if (notificationSettingDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificationSetting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return notificationSettingService
            .save(notificationSettingDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/notification-settings/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /notification-settings/:id} : Updates an existing notificationSetting.
     *
     * @param id the id of the notificationSettingDTO to save.
     * @param notificationSettingDTO the notificationSettingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationSettingDTO,
     * or with status {@code 400 (Bad Request)} if the notificationSettingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationSettingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<NotificationSettingDTO>> updateNotificationSetting(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody NotificationSettingDTO notificationSettingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NotificationSetting : {}, {}", id, notificationSettingDTO);
        if (notificationSettingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationSettingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return notificationSettingRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return notificationSettingService
                    .update(notificationSettingDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /notification-settings/:id} : Partial updates given fields of an existing notificationSetting, field will ignore if it is null
     *
     * @param id the id of the notificationSettingDTO to save.
     * @param notificationSettingDTO the notificationSettingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationSettingDTO,
     * or with status {@code 400 (Bad Request)} if the notificationSettingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificationSettingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationSettingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<NotificationSettingDTO>> partialUpdateNotificationSetting(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody NotificationSettingDTO notificationSettingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NotificationSetting partially : {}, {}", id, notificationSettingDTO);
        if (notificationSettingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationSettingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return notificationSettingRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<NotificationSettingDTO> result = notificationSettingService.partialUpdate(notificationSettingDTO);

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
     * {@code GET  /notification-settings} : get all the notificationSettings.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationSettings in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<NotificationSettingDTO>>> getAllNotificationSettings(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of NotificationSettings");
        return notificationSettingService
            .countAll()
            .zipWith(notificationSettingService.findAll(pageable).collectList())
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
     * {@code GET  /notification-settings/:id} : get the "id" notificationSetting.
     *
     * @param id the id of the notificationSettingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationSettingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<NotificationSettingDTO>> getNotificationSetting(@PathVariable("id") String id) {
        LOG.debug("REST request to get NotificationSetting : {}", id);
        Mono<NotificationSettingDTO> notificationSettingDTO = notificationSettingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationSettingDTO);
    }

    /**
     * {@code DELETE  /notification-settings/:id} : delete the "id" notificationSetting.
     *
     * @param id the id of the notificationSettingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteNotificationSetting(@PathVariable("id") String id) {
        LOG.debug("REST request to delete NotificationSetting : {}", id);
        return notificationSettingService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
