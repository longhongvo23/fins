package com.stockapp.newsservice.web.rest;

import com.stockapp.newsservice.domain.CompanyNews;
import com.stockapp.newsservice.service.NewsIngestionService;
import com.stockapp.newsservice.service.dto.CompanyNewsDTO;
import com.stockapp.newsservice.service.mapper.CompanyNewsMapper;
import com.stockapp.newsservice.web.rest.vm.NewsListVM;
import com.stockapp.newsservice.web.rest.vm.NewsDetailVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Public REST API for News data - designed for mobile/web consumption
 * No authentication required for read operations
 */
@RestController
@RequestMapping("/api/public/news")
@Tag(name = "Public News API", description = "Public endpoints for financial news articles")
public class PublicNewsResource {

    private static final Logger LOG = LoggerFactory.getLogger(PublicNewsResource.class);

    private final NewsIngestionService newsIngestionService;
    private final CompanyNewsMapper companyNewsMapper;

    public PublicNewsResource(
            NewsIngestionService newsIngestionService,
            CompanyNewsMapper companyNewsMapper) {
        this.newsIngestionService = newsIngestionService;
        this.companyNewsMapper = companyNewsMapper;
    }

    /**
     * GET /api/public/news : Get latest news articles
     *
     * @param limit maximum number of articles (default: 50)
     * @return list of news articles
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<NewsListVM> getLatestNews(@RequestParam(defaultValue = "50") int limit) {
        LOG.debug("Public API request to get latest {} news articles", limit);

        return newsIngestionService.getLatestNews(limit)
                .map(this::toNewsListVM);
    }

    /**
     * GET /api/public/news/{id} : Get news article detail
     *
     * @param id the news article UUID
     * @return the news detail
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<NewsDetailVM>> getNewsDetail(@PathVariable String id) {
        LOG.debug("Public API request to get news detail: {}", id);

        return newsIngestionService.getLatestNews(10000) // Get all to search by UUID
                .filter(news -> news.getUuid().equals(id))
                .next()
                .map(this::toNewsDetailVM)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/public/news/symbol/{symbol} : Get news for a specific stock
     *
     * @param symbol the stock symbol
     * @param limit  maximum number of articles (default: 20)
     * @return list of news articles for the symbol
     */
    @GetMapping("/symbol/{symbol}")
    public Flux<NewsListVM> getNewsBySymbol(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "20") int limit) {

        LOG.debug("Public API request to get news for symbol: {}", symbol);

        return newsIngestionService.getNewsBySymbol(symbol.toUpperCase(), limit)
                .map(this::toNewsListVM);
    }

    /**
     * GET /api/public/news/trending : Get trending/most recent news
     *
     * @param hours time window in hours (default: 24)
     * @param limit maximum number of articles (default: 10)
     * @return trending news articles
     */
    @GetMapping("/trending")
    public Flux<NewsListVM> getTrendingNews(
            @RequestParam(defaultValue = "24") int hours,
            @RequestParam(defaultValue = "10") int limit) {

        LOG.debug("Public API request to get trending news from last {} hours", hours);

        // Get recent news from the specified time window
        return newsIngestionService.getLatestNews(limit * 5) // Get more to filter
                .filter(news -> {
                    long hoursAgo = java.time.Duration.between(
                            news.getPublishedAt(),
                            java.time.Instant.now()).toHours();
                    return hoursAgo <= hours;
                })
                .take(limit)
                .map(this::toNewsListVM);
    }

    /**
     * GET /api/public/news/search : Search news articles
     *
     * @param q     search query (title, description, keywords)
     * @param limit maximum number of results (default: 20)
     * @return search results
     */
    @GetMapping("/search")
    public Flux<NewsListVM> searchNews(
            @RequestParam String q,
            @RequestParam(defaultValue = "20") int limit) {

        LOG.debug("Public API request to search news: query={}", q);

        String query = q.toLowerCase();

        return newsIngestionService.getLatestNews(500) // Search in recent news
                .filter(news -> (news.getTitle() != null && news.getTitle().toLowerCase().contains(query)) ||
                        (news.getDescription() != null && news.getDescription().toLowerCase().contains(query)) ||
                        (news.getKeywords() != null && news.getKeywords().toLowerCase().contains(query)))
                .take(limit)
                .map(this::toNewsListVM);
    }

    // Helper methods to convert entities to VMs

    private NewsListVM toNewsListVM(CompanyNews news) {
        NewsListVM vm = new NewsListVM();
        vm.setId(news.getUuid());
        vm.setTitle(news.getTitle());
        vm.setDescription(news.getDescription());
        vm.setSnippet(news.getSnippet());
        vm.setImageUrl(news.getImageUrl());
        vm.setSource(news.getSource());
        vm.setPublishedAt(news.getPublishedAt());

        // Extract symbols from entities
        if (news.getEntities() != null && !news.getEntities().isEmpty()) {
            vm.setSymbols(news.getEntities().stream()
                    .map(entity -> entity.getSymbol())
                    .filter(symbol -> symbol != null)
                    .toList());
        }

        return vm;
    }

    private NewsDetailVM toNewsDetailVM(CompanyNews news) {
        NewsDetailVM vm = new NewsDetailVM();
        vm.setId(news.getUuid());
        vm.setTitle(news.getTitle());
        vm.setDescription(news.getDescription());
        vm.setSnippet(news.getSnippet());
        vm.setUrl(news.getUrl());
        vm.setImageUrl(news.getImageUrl());
        vm.setLanguage(news.getLanguage());
        vm.setSource(news.getSource());
        vm.setKeywords(news.getKeywords());
        vm.setPublishedAt(news.getPublishedAt());
        vm.setRelevanceScore(news.getRelevanceScore());

        // Convert entities to simple DTO
        if (news.getEntities() != null && !news.getEntities().isEmpty()) {
            vm.setRelatedCompanies(news.getEntities().stream()
                    .map(entity -> {
                        NewsDetailVM.RelatedCompany company = new NewsDetailVM.RelatedCompany();
                        company.setSymbol(entity.getSymbol());
                        company.setName(entity.getName());
                        company.setExchange(entity.getExchange());
                        return company;
                    })
                    .toList());
        }

        return vm;
    }
}
