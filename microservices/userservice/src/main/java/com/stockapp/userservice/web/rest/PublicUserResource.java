package com.stockapp.userservice.web.rest;

import com.stockapp.userservice.service.AppUserService;
import com.stockapp.userservice.service.UserProfileService;
import com.stockapp.userservice.service.dto.AppUserDTO;
import com.stockapp.userservice.service.dto.UserProfileDTO;
import com.stockapp.userservice.web.rest.vm.UserProfileVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Public REST API for User Profile management
 * Requires authentication
 */
@RestController
@RequestMapping("/api/public/users")
@Tag(name = "User Profile API", description = "Endpoints for user profile management")
@SecurityRequirement(name = "bearer-jwt")
public class PublicUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(PublicUserResource.class);

    private final AppUserService appUserService;
    private final UserProfileService userProfileService;

    public PublicUserResource(AppUserService appUserService, UserProfileService userProfileService) {
        this.appUserService = appUserService;
        this.userProfileService = userProfileService;
    }

    /**
     * GET /api/public/users/me : Get current user profile
     *
     * @return the current user profile
     */
    @GetMapping("/me")
    public Mono<ResponseEntity<UserProfileVM>> getCurrentUserProfile() {
        LOG.debug("Public API request to get current user profile");

        return getCurrentUserId()
                .flatMap(appUserService::findOne)
                .flatMap(this::toUserProfileVM)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/public/users/me : Update current user profile
     *
     * @param profileVM the profile data to update
     * @return the updated profile
     */
    @PutMapping(value = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<UserProfileVM>> updateCurrentUserProfile(@Valid @RequestBody UserProfileVM profileVM) {
        LOG.debug("Public API request to update current user profile : {}", profileVM);

        return getCurrentUserId()
                .flatMap(userId -> {
                    // Update AppUser fields
                    Mono<AppUserDTO> userUpdateMono = appUserService.findOne(userId)
                            .flatMap(userDTO -> {
                                if (profileVM.getLanguage() != null) {
                                    userDTO.setLanguage(profileVM.getLanguage());
                                }
                                if (profileVM.getTimezone() != null) {
                                    userDTO.setTimezone(profileVM.getTimezone());
                                }
                                return appUserService.update(userDTO);
                            });

                    // Update or create UserProfile
                    Mono<UserProfileDTO> profileUpdateMono = userProfileService.findByUserId(userId)
                            .flatMap(profile -> {
                                updateProfileFields(profile, profileVM);
                                return userProfileService.update(profile);
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                UserProfileDTO newProfile = new UserProfileDTO();
                                updateProfileFields(newProfile, profileVM);
                                return appUserService.findOne(userId)
                                        .flatMap(user -> {
                                            newProfile.setUser(user);
                                            return userProfileService.save(newProfile);
                                        });
                            }));

                    return Mono.zip(userUpdateMono, profileUpdateMono)
                            .then(appUserService.findOne(userId));
                })
                .flatMap(this::toUserProfileVM)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    // Helper methods

    private Mono<String> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .switchIfEmpty(Mono.error(new RuntimeException("User not authenticated")));
    }

    private Mono<UserProfileVM> toUserProfileVM(AppUserDTO userDTO) {
        UserProfileVM vm = new UserProfileVM();
        vm.setLogin(userDTO.getLogin());
        vm.setEmail(userDTO.getEmail());
        vm.setEmailVerified(userDTO.getEmailVerified());
        vm.setLanguage(userDTO.getLanguage());
        vm.setTimezone(userDTO.getTimezone());

        // Fetch and add profile data if exists
        return userProfileService.findByUserId(userDTO.getId())
                .map(profile -> {
                    vm.setFullName(profile.getFullName());
                    vm.setPhoneNumber(profile.getPhoneNumber());
                    vm.setPhoneVerified(profile.getPhoneVerified());
                    vm.setCountry(profile.getCountry());
                    vm.setAvatarUrl(profile.getAvatarUrl());
                    vm.setDateOfBirth(profile.getDateOfBirth());
                    vm.setBio(profile.getBio());
                    vm.setProfileVisibility(profile.getProfileVisibility());
                    vm.setShowEmail(profile.getShowEmail());
                    vm.setShowPhone(profile.getShowPhone());
                    return vm;
                })
                .defaultIfEmpty(vm);
    }

    private void updateProfileFields(UserProfileDTO profile, UserProfileVM vm) {
        if (vm.getFullName() != null) {
            profile.setFullName(vm.getFullName());
        }
        if (vm.getPhoneNumber() != null) {
            profile.setPhoneNumber(vm.getPhoneNumber());
        }
        if (vm.getCountry() != null) {
            profile.setCountry(vm.getCountry());
        }
        if (vm.getAvatarUrl() != null) {
            profile.setAvatarUrl(vm.getAvatarUrl());
        }
        if (vm.getDateOfBirth() != null) {
            profile.setDateOfBirth(vm.getDateOfBirth());
        }
        if (vm.getBio() != null) {
            profile.setBio(vm.getBio());
        }
        if (vm.getProfileVisibility() != null) {
            profile.setProfileVisibility(vm.getProfileVisibility());
        }
        if (vm.getShowEmail() != null) {
            profile.setShowEmail(vm.getShowEmail());
        }
        if (vm.getShowPhone() != null) {
            profile.setShowPhone(vm.getShowPhone());
        }
    }
}
