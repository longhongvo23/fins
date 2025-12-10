package com.stockapp.userservice.web.rest;

import com.stockapp.userservice.service.WatchlistItemService;
import com.stockapp.userservice.service.dto.WatchlistItemDTO;
import com.stockapp.userservice.web.rest.vm.WatchlistVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Public REST API for Watchlist management
 * Requires authentication
 */
@RestController
@RequestMapping("/api/public/watchlist")
@Tag(name = "Watchlist API", description = "Endpoints for managing user watchlists")
@SecurityRequirement(name = "bearer-jwt")
public class PublicWatchlistResource {

    private static final Logger LOG = LoggerFactory.getLogger(PublicWatchlistResource.class);

    private final WatchlistItemService watchlistItemService;

    public PublicWatchlistResource(WatchlistItemService watchlistItemService) {
        this.watchlistItemService = watchlistItemService;
    }

    /**
     * GET /api/public/watchlist : Get user's watchlist
     *
     * @return list of watchlist items
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<WatchlistVM> getUserWatchlist() {
        LOG.debug("Public API request to get user watchlist");

        return getCurrentUserId()
                .flatMapMany(watchlistItemService::findByUserId)
                .map(this::toWatchlistVM);
    }

    /**
     * POST /api/public/watchlist/{symbol} : Add symbol to watchlist
     *
     * @param symbol the stock symbol to add
     * @return the created watchlist item
     */
    @PostMapping("/{symbol}")
    public Mono<ResponseEntity<WatchlistVM>> addToWatchlist(@PathVariable String symbol) {
        LOG.debug("Public API request to add {} to watchlist", symbol);

        return getCurrentUserId()
                .flatMap(userId -> watchlistItemService.addToWatchlist(userId, symbol.toUpperCase()))
                .map(this::toWatchlistVM)
                .map(vm -> ResponseEntity.status(HttpStatus.CREATED).body(vm))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    /**
     * DELETE /api/public/watchlist/{symbol} : Remove symbol from watchlist
     *
     * @param symbol the stock symbol to remove
     * @return status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{symbol}")
    public Mono<ResponseEntity<Void>> removeFromWatchlist(@PathVariable String symbol) {
        LOG.debug("Public API request to remove {} from watchlist", symbol);

        return getCurrentUserId()
                .flatMap(userId -> watchlistItemService.removeFromWatchlist(userId, symbol.toUpperCase()))
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/public/watchlist/check/{symbol} : Check if symbol is in watchlist
     *
     * @param symbol the stock symbol to check
     * @return true if in watchlist, false otherwise
     */
    @GetMapping("/check/{symbol}")
    public Mono<ResponseEntity<Boolean>> checkInWatchlist(@PathVariable String symbol) {
        LOG.debug("Public API request to check if {} is in watchlist", symbol);

        return getCurrentUserId()
                .flatMap(userId -> watchlistItemService.isInWatchlist(userId, symbol.toUpperCase()))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(false));
    }

    // Helper methods

    private Mono<String> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .switchIfEmpty(Mono.error(new RuntimeException("User not authenticated")));
    }

    private WatchlistVM toWatchlistVM(WatchlistItemDTO item) {
        WatchlistVM vm = new WatchlistVM();
        vm.setSymbol(item.getSymbol());
        vm.setAddedAt(item.getAddedAt());
        return vm;
    }
}
