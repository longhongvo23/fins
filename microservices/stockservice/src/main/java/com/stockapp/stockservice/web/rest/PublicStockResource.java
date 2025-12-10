package com.stockapp.stockservice.web.rest;

import com.stockapp.stockservice.service.CompanyService;
import com.stockapp.stockservice.service.FinancialService;
import com.stockapp.stockservice.service.HistoricalPriceService;
import com.stockapp.stockservice.service.StockStatisticsService;
import com.stockapp.stockservice.service.IntradayQuoteService;
import com.stockapp.stockservice.service.dto.CompanyDTO;
import com.stockapp.stockservice.service.dto.HistoricalPriceDTO;
import com.stockapp.stockservice.service.dto.StockStatisticsDTO;
import com.stockapp.stockservice.service.dto.FinancialDTO;
import com.stockapp.stockservice.service.dto.IntradayQuoteDTO;
import com.stockapp.stockservice.web.rest.vm.StockQuoteVM;
import com.stockapp.stockservice.web.rest.vm.StockSearchResultVM;
import com.stockapp.stockservice.web.rest.vm.TrendingStockVM;
import com.stockapp.stockservice.web.rest.vm.StockDetailedStatisticsVM;
import com.stockapp.stockservice.web.rest.vm.FinancialStatementVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

/**
 * Public REST API for Stock data - designed for mobile/web consumption
 * No authentication required for read operations
 */
@RestController
@RequestMapping("/api/public/stocks")
@Tag(name = "Public Stock API", description = "Public endpoints for stock market data")
public class PublicStockResource {

    private static final Logger LOG = LoggerFactory.getLogger(PublicStockResource.class);

    private final CompanyService companyService;
    private final HistoricalPriceService historicalPriceService;
    private final StockStatisticsService stockStatisticsService;
    private final FinancialService financialService;
    private final IntradayQuoteService intradayQuoteService;

    public PublicStockResource(
            CompanyService companyService,
            HistoricalPriceService historicalPriceService,
            StockStatisticsService stockStatisticsService,
            FinancialService financialService,
            IntradayQuoteService intradayQuoteService) {
        this.companyService = companyService;
        this.historicalPriceService = historicalPriceService;
        this.stockStatisticsService = stockStatisticsService;
        this.financialService = financialService;
        this.intradayQuoteService = intradayQuoteService;
    }

    /**
     * GET /api/public/stocks : Get all stocks
     *
     * @return list of all companies/stocks
     */
    @Operation(summary = "Get all stocks", description = "Retrieve all available stocks/companies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stocks")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<CompanyDTO> getAllStocks() {
        LOG.debug("Public API request to get all stocks");
        return companyService.findAll();
    }

    /**
     * GET /api/public/stocks/{symbol} : Get stock detail by symbol
     *
     * @param symbol the stock symbol (e.g., AAPL)
     * @return the stock details
     */
    @Operation(summary = "Get stock by symbol", description = "Retrieve detailed information for a specific stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock found"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    @GetMapping("/{symbol}")
    public Mono<ResponseEntity<CompanyDTO>> getStock(
            @Parameter(description = "Stock symbol (e.g., FPT, VNM)", required = true) @PathVariable String symbol) {
        LOG.debug("Public API request to get stock: {}", symbol);
        return companyService.findBySymbol(symbol)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/public/stocks/{symbol}/quote : Get latest quote for a stock
     *
     * @param symbol the stock symbol
     * @return the latest price data
     */
    @GetMapping("/{symbol}/quote")
    public Mono<ResponseEntity<StockQuoteVM>> getStockQuote(@PathVariable String symbol) {
        LOG.debug("Public API request to get quote for: {}", symbol);

        return Mono.zip(
                companyService.findBySymbol(symbol),
                historicalPriceService.findLatestBySymbol(symbol)).map(tuple -> {
                    CompanyDTO company = tuple.getT1();
                    HistoricalPriceDTO latestPrice = tuple.getT2();

                    StockQuoteVM quote = new StockQuoteVM();
                    quote.setSymbol(company.getSymbol());
                    quote.setName(company.getName());
                    quote.setCurrentPrice(latestPrice.getClose());
                    quote.setOpen(latestPrice.getOpen());
                    quote.setHigh(latestPrice.getHigh());
                    quote.setLow(latestPrice.getLow());
                    quote.setVolume(latestPrice.getVolume());
                    quote.setDatetime(latestPrice.getDatetime());

                    return ResponseEntity.ok(quote);
                }).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/public/stocks/{symbol}/history : Get historical data
     *
     * @param symbol   the stock symbol
     * @param from     start date (optional)
     * @param to       end date (optional)
     * @param interval data interval (default: 1day)
     * @param page     page number (default: 0)
     * @param size     page size (default: 100)
     * @return historical price data
     */
    @GetMapping("/{symbol}/history")
    public Flux<HistoricalPriceDTO> getStockHistory(
            @PathVariable String symbol,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "1day") String interval,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {

        LOG.debug("Public API request to get history for {} from {} to {}", symbol, from, to);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "datetime"));

        if (from != null && to != null) {
            return historicalPriceService.findBySymbolAndDateRange(
                    symbol,
                    from.toString(),
                    to.toString(),
                    pageable);
        } else {
            return historicalPriceService.findBySymbol(symbol, pageable);
        }
    }

    /**
     * GET /api/public/stocks/{symbol}/chart : Get chart data optimized for mobile
     *
     * @param symbol the stock symbol
     * @param period time period (1D, 1W, 1M, 3M, 6M, 1Y, 5Y, MAX)
     * @return chart data points
     */
    @GetMapping("/{symbol}/chart")
    public Flux<HistoricalPriceDTO> getStockChart(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "1M") String period) {

        LOG.debug("Public API request to get chart data for {} with period {}", symbol, period);

        LocalDate to = LocalDate.now();
        LocalDate from = calculateFromDate(to, period);
        int limit = calculateDataPoints(period);

        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.ASC, "datetime"));

        return historicalPriceService.findBySymbolAndDateRange(
                symbol,
                from.toString(),
                to.toString(),
                pageable);
    }

    /**
     * GET /api/public/stocks/trending : Get trending stocks (top gainers/losers)
     *
     * @param type  gainers or losers (default: gainers)
     * @param limit number of results (default: 10)
     * @return list of trending stocks
     */
    @GetMapping("/trending")
    public Flux<TrendingStockVM> getTrendingStocks(
            @RequestParam(defaultValue = "gainers") String type,
            @RequestParam(defaultValue = "10") int limit) {

        LOG.debug("Public API request to get trending stocks: type={}, limit={}", type, limit);

        return companyService.findAll()
                .flatMap(company -> historicalPriceService.findLatestBySymbol(company.getSymbol())
                        .zipWith(historicalPriceService.findPreviousBySymbol(company.getSymbol()))
                        .map(tuple -> {
                            HistoricalPriceDTO latest = tuple.getT1();
                            HistoricalPriceDTO previous = tuple.getT2();

                            TrendingStockVM trending = new TrendingStockVM();
                            trending.setSymbol(company.getSymbol());
                            trending.setName(company.getName());
                            trending.setCurrentPrice(latest.getClose());
                            trending.setPreviousClose(previous.getClose());
                            trending.calculateChange();

                            return trending;
                        }))
                .sort((a, b) -> {
                    if ("gainers".equals(type)) {
                        return b.getChangePercent().compareTo(a.getChangePercent());
                    } else {
                        return a.getChangePercent().compareTo(b.getChangePercent());
                    }
                })
                .take(limit);
    }

    /**
     * GET /api/public/stocks/search : Search stocks by symbol or name
     *
     * @param q     search query
     * @param limit max results (default: 10)
     * @return search results
     */
    @GetMapping("/search")
    public Flux<StockSearchResultVM> searchStocks(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit) {

        LOG.debug("Public API request to search stocks: query={}", q);

        String query = q.toUpperCase();

        return companyService.findAll()
                .filter(company -> company.getSymbol().contains(query) ||
                        (company.getName() != null && company.getName().toUpperCase().contains(query)))
                .take(limit)
                .map(company -> {
                    StockSearchResultVM result = new StockSearchResultVM();
                    result.setSymbol(company.getSymbol());
                    result.setName(company.getName());
                    result.setExchange(company.getExchange());
                    result.setLogo(company.getLogo());
                    return result;
                });
    }

    // Helper methods

    private LocalDate calculateFromDate(LocalDate to, String period) {
        return switch (period) {
            case "1D" -> to.minusDays(1);
            case "1W" -> to.minusWeeks(1);
            case "1M" -> to.minusMonths(1);
            case "3M" -> to.minusMonths(3);
            case "6M" -> to.minusMonths(6);
            case "1Y" -> to.minusYears(1);
            case "5Y" -> to.minusYears(5);
            case "MAX" -> LocalDate.of(2015, 1, 1); // Our data starts from 2015
            default -> to.minusMonths(1);
        };
    }

    private int calculateDataPoints(String period) {
        return switch (period) {
            case "1D" -> 1;
            case "1W" -> 7;
            case "1M" -> 30;
            case "3M" -> 90;
            case "6M" -> 180;
            case "1Y" -> 365;
            case "5Y" -> 1825;
            case "MAX" -> 5000; // ~10 years of daily data
            default -> 30;
        };
    }

    /**
     * GET /api/public/stocks/{symbol}/statistics : Get detailed statistics for a
     * stock
     *
     * @param symbol the stock symbol
     * @return detailed statistics
     */
    @GetMapping("/{symbol}/statistics")
    @Operation(summary = "Get stock statistics", description = "Get detailed statistics and metrics for a stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    public Mono<ResponseEntity<StockDetailedStatisticsVM>> getStockStatistics(@PathVariable String symbol) {
        LOG.debug("Public API request to get statistics for: {}", symbol);

        return Mono.zip(
                companyService.findBySymbol(symbol),
                stockStatisticsService.findBySymbol(symbol)
                        .defaultIfEmpty(new StockStatisticsDTO()))
                .map(tuple -> {
                    CompanyDTO company = tuple.getT1();
                    StockStatisticsDTO stats = tuple.getT2();

                    StockDetailedStatisticsVM vm = new StockDetailedStatisticsVM();
                    vm.setSymbol(company.getSymbol());
                    vm.setName(company.getName());
                    vm.setMarketCap(company.getMarketCapitalization());
                    vm.setSharesOutstanding(company.getShareOutstanding());
                    vm.setFiftyTwoWeekHigh(stats.getFiftyTwoWeekHigh());
                    vm.setFiftyTwoWeekLow(stats.getFiftyTwoWeekLow());
                    vm.setAverageVolume(stats.getAverageVolume());

                    return ResponseEntity.ok(vm);
                }).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/public/stocks/{symbol}/financials : Get financial statements for a
     * stock
     *
     * @param symbol the stock symbol
     * @param type   statement type (BS, IC, CF) - optional
     * @param period frequency (ANNUAL, QUARTERLY) - optional
     * @return financial statements
     */
    @GetMapping("/{symbol}/financials")
    @Operation(summary = "Get financial statements", description = "Get financial statements for a stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Financial statements retrieved"),
            @ApiResponse(responseCode = "404", description = "No financial data found")
    })
    public Flux<FinancialDTO> getFinancials(
            @PathVariable String symbol,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String period) {
        LOG.debug("Public API request to get financials for: {}, type: {}, period: {}", symbol, type, period);

        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "year"));
        return financialService.findBySymbol(symbol, pageable);
    }

    /**
     * GET /api/public/stocks/{symbol}/intraday : Get intraday quote data
     *
     * @param symbol the stock symbol
     * @return intraday quote
     */
    @GetMapping("/{symbol}/intraday")
    @Operation(summary = "Get intraday quote", description = "Get real-time or latest intraday quote for a stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Intraday quote retrieved"),
            @ApiResponse(responseCode = "404", description = "Quote not found")
    })
    public Mono<ResponseEntity<IntradayQuoteDTO>> getIntradayQuote(@PathVariable String symbol) {
        LOG.debug("Public API request to get intraday quote for: {}", symbol);

        return intradayQuoteService.findBySymbol(symbol)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/public/stocks/gainers : Get top gaining stocks
     *
     * @param limit number of results (default: 10)
     * @return top gainers
     */
    @GetMapping("/gainers")
    @Operation(summary = "Get top gainers", description = "Get top gaining stocks of the day")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top gainers retrieved")
    })
    public Flux<TrendingStockVM> getTopGainers(@RequestParam(defaultValue = "10") int limit) {
        LOG.debug("Public API request to get top {} gainers", limit);
        return getTrendingStocks("gainers", limit);
    }

    /**
     * GET /api/public/stocks/losers : Get top losing stocks
     *
     * @param limit number of results (default: 10)
     * @return top losers
     */
    @GetMapping("/losers")
    @Operation(summary = "Get top losers", description = "Get top losing stocks of the day")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top losers retrieved")
    })
    public Flux<TrendingStockVM> getTopLosers(@RequestParam(defaultValue = "10") int limit) {
        LOG.debug("Public API request to get top {} losers", limit);
        return getTrendingStocks("losers", limit);
    }

    /**
     * GET /api/public/stocks/most-active : Get most actively traded stocks
     *
     * @param limit number of results (default: 10)
     * @return most active stocks
     */
    @GetMapping("/most-active")
    @Operation(summary = "Get most active stocks", description = "Get most actively traded stocks by volume")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Most active stocks retrieved")
    })
    public Flux<TrendingStockVM> getMostActive(@RequestParam(defaultValue = "10") int limit) {
        LOG.debug("Public API request to get {} most active stocks", limit);

        return companyService.findAll()
                .flatMap(company -> historicalPriceService.findLatestBySymbol(company.getSymbol())
                        .map(latest -> {
                            TrendingStockVM vm = new TrendingStockVM();
                            vm.setSymbol(company.getSymbol());
                            vm.setName(company.getName());
                            vm.setCurrentPrice(latest.getClose());
                            vm.setVolume(latest.getVolume());
                            return vm;
                        }))
                .sort((a, b) -> Long.compare(b.getVolume() != null ? b.getVolume() : 0L,
                        a.getVolume() != null ? a.getVolume() : 0L))
                .take(limit);
    }

    /**
     * GET /api/public/stocks/sectors : Get list of sectors
     *
     * @return list of sectors
     */
    @GetMapping("/sectors")
    @Operation(summary = "Get sectors", description = "Get list of available stock sectors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sectors retrieved")
    })
    public Flux<String> getSectors() {
        LOG.debug("Public API request to get sectors");

        return companyService.findAll()
                .map(CompanyDTO::getFinnhubIndustry)
                .filter(sector -> sector != null && !sector.isEmpty())
                .distinct()
                .sort();
    }

    /**
     * GET /api/public/stocks/exchanges : Get list of exchanges
     *
     * @return list of exchanges
     */
    @GetMapping("/exchanges")
    @Operation(summary = "Get exchanges", description = "Get list of available stock exchanges")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchanges retrieved")
    })
    public Flux<String> getExchanges() {
        LOG.debug("Public API request to get exchanges");

        return companyService.findAll()
                .map(CompanyDTO::getExchange)
                .filter(exchange -> exchange != null && !exchange.isEmpty())
                .distinct()
                .sort();
    }

    /**
     * GET /api/public/stocks/sector/{sector} : Get stocks by sector
     *
     * @param sector the sector name
     * @param limit  max results (default: 50)
     * @return stocks in the sector
     */
    @GetMapping("/sector/{sector}")
    @Operation(summary = "Get stocks by sector", description = "Get all stocks in a specific sector")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stocks retrieved")
    })
    public Flux<CompanyDTO> getStocksBySector(
            @PathVariable String sector,
            @RequestParam(defaultValue = "50") int limit) {
        LOG.debug("Public API request to get stocks in sector: {}", sector);

        return companyService.findAll()
                .filter(company -> sector.equalsIgnoreCase(company.getFinnhubIndustry()))
                .take(limit);
    }
}
