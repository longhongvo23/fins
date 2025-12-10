package com.stockapp.userservice.web.rest;

import com.stockapp.userservice.domain.enumeration.AccountStatus;
import com.stockapp.userservice.service.AppUserService;
import com.stockapp.userservice.service.dto.AppUserDTO;
import com.stockapp.userservice.web.rest.vm.MessageResponseVM;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

/**
 * REST controller for Admin User Management
 * 
 * This controller provides endpoints for administrators to:
 * - List all users with pagination and search
 * - Get user details
 * - Create new users
 * - Update user information
 * - Delete users
 * - Activate/deactivate users
 * - Reset user passwords
 * - Get user statistics
 * 
 * All endpoints require ADMIN role
 */
@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "Admin User Management API", description = "Endpoints for administrators to manage users")
@SecurityRequirement(name = "bearer-jwt")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdminUserResource.class);

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    public AdminUserResource(AppUserService appUserService, PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * GET /api/admin/users : Get all users with pagination
     *
     * @param page   page number (default: 0)
     * @param size   page size (default: 20)
     * @param sort   sort criteria (default: createdDate,desc)
     * @param search search keyword for username or email
     * @return paginated list of users
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all users", description = "Retrieve paginated list of users with optional search")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin role required")
    })
    public Flux<AppUserDTO> getAllUsers(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort criteria") @RequestParam(defaultValue = "createdDate,desc") String sort,
            @Parameter(description = "Search keyword") @RequestParam(required = false) String search) {

        LOG.debug("REST request to get all users - page: {}, size: {}, search: {}", page, size, search);

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Flux<AppUserDTO> usersFlux = appUserService.findAll(pageable);

        // Apply search filter if provided
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase();
            usersFlux = usersFlux
                    .filter(user -> (user.getLogin() != null && user.getLogin().toLowerCase().contains(searchLower)) ||
                            (user.getEmail() != null && user.getEmail().toLowerCase().contains(searchLower)));
        }

        return usersFlux;
    }

    /**
     * GET /api/admin/users/count : Get total count of users
     *
     * @return total count
     */
    @GetMapping("/count")
    @Operation(summary = "Get total user count", description = "Get total number of registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved count")
    })
    public Mono<ResponseEntity<Long>> getUserCount() {
        LOG.debug("REST request to get user count");
        return appUserService.countAll()
                .map(ResponseEntity::ok);
    }

    /**
     * GET /api/admin/users/{id} : Get user by ID
     *
     * @param id the user ID
     * @return the user details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve detailed information of a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<ResponseEntity<AppUserDTO>> getUser(
            @Parameter(description = "User ID", required = true) @PathVariable String id) {
        LOG.debug("REST request to get User : {}", id);
        return appUserService.findOne(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/admin/users : Create a new user
     *
     * @param userDTO the user to create
     * @return the created user
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create user", description = "Create a new user account by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data or user already exists")
    })
    public Mono<ResponseEntity<AppUserDTO>> createUser(@Valid @RequestBody AppUserDTO userDTO) {
        LOG.debug("REST request to create User : {}", userDTO);

        if (userDTO.getId() != null) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        // Check if login already exists
        return appUserService.findByLogin(userDTO.getLogin())
                .flatMap(existingUser -> Mono.just(ResponseEntity.badRequest().<AppUserDTO>build()))
                .switchIfEmpty(Mono.defer(() ->
                // Check if email already exists
                appUserService.findByEmail(userDTO.getEmail())
                        .flatMap(existingUser -> Mono.just(ResponseEntity.badRequest().<AppUserDTO>build()))
                        .switchIfEmpty(Mono.defer(() -> {
                            // Set default values
                            userDTO.setCreatedDate(Instant.now());
                            if (userDTO.getPassword() != null) {
                                userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                            }
                            if (userDTO.getActivated() == null) {
                                userDTO.setActivated(true);
                            }
                            if (userDTO.getAccountStatus() == null) {
                                userDTO.setAccountStatus(AccountStatus.ACTIVE);
                            }
                            if (userDTO.getEmailVerified() == null) {
                                userDTO.setEmailVerified(false);
                            }
                            if (userDTO.getTwoFactorEnabled() == null) {
                                userDTO.setTwoFactorEnabled(false);
                            }
                            if (userDTO.getFailedLoginAttempts() == null) {
                                userDTO.setFailedLoginAttempts(0);
                            }

                            return appUserService.save(userDTO)
                                    .map(savedUser -> ResponseEntity.status(HttpStatus.CREATED).body(savedUser));
                        }))));
    }

    /**
     * PUT /api/admin/users/{id} : Update an existing user
     *
     * @param id      the user ID
     * @param userDTO the user to update
     * @return the updated user
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update user", description = "Update an existing user's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<ResponseEntity<AppUserDTO>> updateUser(
            @Parameter(description = "User ID", required = true) @PathVariable String id,
            @Valid @RequestBody AppUserDTO userDTO) {
        LOG.debug("REST request to update User : {}, {}", id, userDTO);

        if (!id.equals(userDTO.getId())) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return appUserService.findOne(id)
                .flatMap(existingUser -> {
                    userDTO.setLastModifiedDate(Instant.now());
                    // Don't allow password update through this endpoint
                    userDTO.setPassword(existingUser.getPassword());
                    return appUserService.update(userDTO);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * PATCH /api/admin/users/{id} : Partially update user
     *
     * @param id      the user ID
     * @param userDTO the user fields to update
     * @return the updated user
     */
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Partially update user", description = "Update specific fields of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<ResponseEntity<AppUserDTO>> partialUpdateUser(
            @Parameter(description = "User ID", required = true) @PathVariable String id,
            @RequestBody AppUserDTO userDTO) {
        LOG.debug("REST request to partially update User : {}, {}", id, userDTO);

        userDTO.setId(id);
        return appUserService.partialUpdate(userDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/admin/users/{id} : Delete a user
     *
     * @param id the user ID
     * @return status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Permanently delete a user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<ResponseEntity<Void>> deleteUser(
            @Parameter(description = "User ID", required = true) @PathVariable String id) {
        LOG.debug("REST request to delete User : {}", id);
        return appUserService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    /**
     * PATCH /api/admin/users/{id}/activate : Activate a user account
     *
     * @param id the user ID
     * @return the updated user
     */
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate user", description = "Activate a user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<ResponseEntity<AppUserDTO>> activateUser(
            @Parameter(description = "User ID", required = true) @PathVariable String id) {
        LOG.debug("REST request to activate User : {}", id);

        return appUserService.findOne(id)
                .flatMap(user -> {
                    user.setActivated(true);
                    user.setAccountStatus(AccountStatus.ACTIVE);
                    user.setAccountLockedUntil(null);
                    user.setFailedLoginAttempts(0);
                    return appUserService.update(user);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * PATCH /api/admin/users/{id}/deactivate : Deactivate a user account
     *
     * @param id the user ID
     * @return the updated user
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user", description = "Deactivate a user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<ResponseEntity<AppUserDTO>> deactivateUser(
            @Parameter(description = "User ID", required = true) @PathVariable String id) {
        LOG.debug("REST request to deactivate User : {}", id);

        return appUserService.findOne(id)
                .flatMap(user -> {
                    user.setActivated(false);
                    user.setAccountStatus(AccountStatus.INACTIVE);
                    return appUserService.update(user);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/admin/users/{id}/reset-password : Reset user password
     *
     * @param id the user ID
     * @return temporary password
     */
    @PostMapping("/{id}/reset-password")
    @Operation(summary = "Reset user password", description = "Generate and set a temporary password for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<ResponseEntity<MessageResponseVM>> resetUserPassword(
            @Parameter(description = "User ID", required = true) @PathVariable String id) {
        LOG.debug("REST request to reset password for User : {}", id);

        return appUserService.findOne(id)
                .flatMap(user -> {
                    // Generate temporary password
                    String tempPassword = UUID.randomUUID().toString().substring(0, 12);
                    user.setPassword(passwordEncoder.encode(tempPassword));
                    user.setLastPasswordChangeDate(Instant.now());
                    user.setFailedLoginAttempts(0);

                    return appUserService.update(user)
                            .doOnSuccess(updatedUser -> {
                                LOG.info("Password reset for user: {}", updatedUser.getLogin());
                                // TODO: Send email with temporary password
                            })
                            .map(updatedUser -> ResponseEntity.ok(
                                    new MessageResponseVM("Temporary password: " + tempPassword)));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/admin/users/statistics : Get user statistics
     *
     * @return user statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get user statistics", description = "Get overall user statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public Mono<ResponseEntity<UserStatisticsVM>> getUserStatistics() {
        LOG.debug("REST request to get user statistics");

        return appUserService.countAll()
                .flatMap(totalUsers -> {
                    Mono<Long> activeUsers = appUserService.findAll(Pageable.unpaged())
                            .filter(user -> user.getActivated() != null && user.getActivated())
                            .count();

                    Mono<Long> inactiveUsers = appUserService.findAll(Pageable.unpaged())
                            .filter(user -> user.getActivated() != null && !user.getActivated())
                            .count();

                    Mono<Long> lockedUsers = appUserService.findAll(Pageable.unpaged())
                            .filter(user -> AccountStatus.LOCKED.equals(user.getAccountStatus()))
                            .count();

                    return Mono.zip(activeUsers, inactiveUsers, lockedUsers)
                            .map(tuple -> {
                                UserStatisticsVM stats = new UserStatisticsVM();
                                stats.setTotalUsers(totalUsers);
                                stats.setActiveUsers(tuple.getT1());
                                stats.setInactiveUsers(tuple.getT2());
                                stats.setLockedUsers(tuple.getT3());
                                return ResponseEntity.ok(stats);
                            });
                });
    }

    /**
     * Inner class for user statistics response
     */
    public static class UserStatisticsVM {
        private Long totalUsers;
        private Long activeUsers;
        private Long inactiveUsers;
        private Long lockedUsers;

        public Long getTotalUsers() {
            return totalUsers;
        }

        public void setTotalUsers(Long totalUsers) {
            this.totalUsers = totalUsers;
        }

        public Long getActiveUsers() {
            return activeUsers;
        }

        public void setActiveUsers(Long activeUsers) {
            this.activeUsers = activeUsers;
        }

        public Long getInactiveUsers() {
            return inactiveUsers;
        }

        public void setInactiveUsers(Long inactiveUsers) {
            this.inactiveUsers = inactiveUsers;
        }

        public Long getLockedUsers() {
            return lockedUsers;
        }

        public void setLockedUsers(Long lockedUsers) {
            this.lockedUsers = lockedUsers;
        }
    }
}
