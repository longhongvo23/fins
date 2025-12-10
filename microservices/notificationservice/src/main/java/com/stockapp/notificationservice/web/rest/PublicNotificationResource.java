package com.stockapp.notificationservice.web.rest;

import com.stockapp.notificationservice.domain.enumeration.NotificationStatus;
import com.stockapp.notificationservice.domain.enumeration.NotificationType;
import com.stockapp.notificationservice.service.NotificationService;
import com.stockapp.notificationservice.service.dto.NotificationDTO;
import com.stockapp.notificationservice.web.rest.vm.NotificationVM;
import com.stockapp.notificationservice.web.rest.vm.PriceAlertVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Public REST API for Notification management
 * Requires authentication
 */
@RestController
@RequestMapping("/api/public/notifications")
@Tag(name = "Notification API", description = "Endpoints for managing user notifications and price alerts")
@SecurityRequirement(name = "bearer-jwt")
public class PublicNotificationResource {

    private static final Logger LOG = LoggerFactory.getLogger(PublicNotificationResource.class);

    private final NotificationService notificationService;

    public PublicNotificationResource(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * GET /api/public/notifications : Get user's notifications
     *
     * @param read filter by read status (optional)
     * @param page page number (default: 0)
     * @param size page size (default: 20)
     * @return paginated list of notifications
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get notifications", description = "Get paginated list of user notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully")
    })
    public Flux<NotificationVM> getUserNotifications(
            @Parameter(description = "Filter by read status") @RequestParam(required = false) Boolean read,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        LOG.debug("Public API request to get notifications - read: {}, page: {}, size: {}", read, page, size);

        return getCurrentUserId()
                .flatMapMany(userId -> {
                    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
                    Flux<NotificationDTO> notifications = notificationService.findByRecipient(userId, pageable);

                    // Filter by read status if provided
                    if (read != null) {
                        notifications = notifications
                                .filter(n -> NotificationStatus.SENT.equals(n.getStatus()) == read);
                    }

                    return notifications.map(this::toNotificationVM);
                });
    }

    /**
     * GET /api/public/notifications/unread-count : Get count of unread
     * notifications
     *
     * @return unread count
     */
    @GetMapping("/unread-count")
    @Operation(summary = "Get unread count", description = "Get count of unread notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public Mono<ResponseEntity<Long>> getUnreadCount() {
        LOG.debug("Public API request to get unread count");

        return getCurrentUserId()
                .flatMap(userId -> notificationService.findByRecipient(userId, Pageable.unpaged())
                        .filter(n -> NotificationStatus.PENDING.equals(n.getStatus()))
                        .count())
                .map(ResponseEntity::ok);
    }

    /**
     * PUT /api/public/notifications/{id}/read : Mark notification as read
     *
     * @param id the notification ID
     * @return updated notification
     */
    @PutMapping("/{id}/read")
    @Operation(summary = "Mark as read", description = "Mark a notification as read")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification marked as read"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public Mono<ResponseEntity<NotificationVM>> markAsRead(@PathVariable String id) {
        LOG.debug("Public API request to mark notification as read: {}", id);

        return notificationService.findOne(id)
                .flatMap(notification -> {
                    notification.setStatus(NotificationStatus.SENT);
                    notification.setSentAt(Instant.now());
                    return notificationService.update(notification);
                })
                .map(this::toNotificationVM)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * PATCH /api/public/notifications/read-all : Mark all notifications as read
     *
     * @return number of notifications updated
     */
    @PatchMapping("/read-all")
    @Operation(summary = "Mark all as read", description = "Mark all user notifications as read")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All notifications marked as read")
    })
    public Mono<ResponseEntity<Map<String, Long>>> markAllAsRead() {
        LOG.debug("Public API request to mark all notifications as read");

        return getCurrentUserId()
                .flatMapMany(userId -> notificationService.findByRecipient(userId, Pageable.unpaged()))
                .filter(n -> NotificationStatus.PENDING.equals(n.getStatus()))
                .flatMap(notification -> {
                    notification.setStatus(NotificationStatus.SENT);
                    notification.setSentAt(Instant.now());
                    return notificationService.update(notification);
                })
                .count()
                .map(count -> {
                    Map<String, Long> result = new HashMap<>();
                    result.put("updated", count);
                    return ResponseEntity.ok(result);
                });
    }

    /**
     * DELETE /api/public/notifications/{id} : Delete a notification
     *
     * @param id the notification ID
     * @return status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification", description = "Delete a notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification deleted"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public Mono<ResponseEntity<Void>> deleteNotification(@PathVariable String id) {
        LOG.debug("Public API request to delete notification: {}", id);

        return notificationService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    /**
     * POST /api/public/notifications/price-alerts : Create a price alert
     *
     * @param alertVM the price alert data
     * @return created notification
     */
    @PostMapping("/price-alerts")
    @Operation(summary = "Create price alert", description = "Create a price alert notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Price alert created"),
            @ApiResponse(responseCode = "400", description = "Invalid alert data")
    })
    public Mono<ResponseEntity<NotificationVM>> createPriceAlert(@Valid @RequestBody PriceAlertVM alertVM) {
        LOG.debug("Public API request to create price alert: {}", alertVM);

        return getCurrentUserId()
                .flatMap(userId -> {
                    NotificationDTO notification = new NotificationDTO();
                    notification.setRecipient(userId);
                    notification.setType(NotificationType.IN_APP);
                    notification.setStatus(NotificationStatus.PENDING);
                    notification.setSubject("Price Alert: " + alertVM.getSymbol());
                    notification.setContent(String.format(
                            "Alert when %s goes %s $%s",
                            alertVM.getSymbol(),
                            alertVM.getCondition(),
                            alertVM.getTargetPrice()));
                    notification.setCreatedAt(Instant.now());

                    return notificationService.save(notification);
                })
                .map(this::toNotificationVM)
                .map(vm -> ResponseEntity.status(HttpStatus.CREATED).body(vm));
    }

    /**
     * GET /api/public/notifications/price-alerts : Get user's price alerts
     *
     * @return list of active price alerts
     */
    @GetMapping("/price-alerts")
    @Operation(summary = "Get price alerts", description = "Get user's active price alerts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Price alerts retrieved")
    })
    public Flux<NotificationVM> getPriceAlerts() {
        LOG.debug("Public API request to get price alerts");

        return getCurrentUserId()
                .flatMapMany(userId -> notificationService.findByRecipient(userId, Pageable.unpaged())
                        .filter(n -> n.getSubject() != null && n.getSubject().startsWith("Price Alert:"))
                        .filter(n -> NotificationStatus.PENDING.equals(n.getStatus())))
                .map(this::toNotificationVM);
    }

    /**
     * DELETE /api/public/notifications/price-alerts/{id} : Delete a price alert
     *
     * @param id the alert notification ID
     * @return status 204 (NO_CONTENT)
     */
    @DeleteMapping("/price-alerts/{id}")
    @Operation(summary = "Delete price alert", description = "Delete a price alert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Price alert deleted")
    })
    public Mono<ResponseEntity<Void>> deletePriceAlert(@PathVariable String id) {
        LOG.debug("Public API request to delete price alert: {}", id);

        return notificationService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    // Helper methods

    private Mono<String> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName);
    }

    private NotificationVM toNotificationVM(NotificationDTO dto) {
        NotificationVM vm = new NotificationVM();
        vm.setId(dto.getId());
        vm.setRecipient(dto.getRecipient());
        vm.setType(dto.getType());
        vm.setStatus(dto.getStatus());
        vm.setSubject(dto.getSubject());
        vm.setContent(dto.getContent());
        vm.setCreatedAt(dto.getCreatedAt());
        vm.setSentAt(dto.getSentAt());
        vm.setRead(NotificationStatus.SENT.equals(dto.getStatus()));
        return vm;
    }
}
