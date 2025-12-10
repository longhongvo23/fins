package com.stockapp.crawlservice.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "application")
public class ApiProperties {

    private Api api;
    private Stock stock;
    private Crawl crawl;

    public static class Api {
        private TwelveData twelveData;
        private Finnhub finnhub;
        private Marketaux marketaux;

        public static class TwelveData {
            private String baseUrl;
            private String apiKey;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public String getApiKey() {
                return apiKey;
            }

            public void setApiKey(String apiKey) {
                this.apiKey = apiKey;
            }
        }

        public static class Finnhub {
            private String baseUrl;
            private String apiKey;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public String getApiKey() {
                return apiKey;
            }

            public void setApiKey(String apiKey) {
                this.apiKey = apiKey;
            }
        }

        public static class Marketaux {
            private String baseUrl;
            private String apiKey;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public String getApiKey() {
                return apiKey;
            }

            public void setApiKey(String apiKey) {
                this.apiKey = apiKey;
            }
        }

        public TwelveData getTwelveData() {
            return twelveData;
        }

        public void setTwelveData(TwelveData twelveData) {
            this.twelveData = twelveData;
        }

        public Finnhub getFinnhub() {
            return finnhub;
        }

        public void setFinnhub(Finnhub finnhub) {
            this.finnhub = finnhub;
        }

        public Marketaux getMarketaux() {
            return marketaux;
        }

        public void setMarketaux(Marketaux marketaux) {
            this.marketaux = marketaux;
        }
    }

    public static class Stock {
        private List<String> symbols;

        public List<String> getSymbols() {
            return symbols;
        }

        public void setSymbols(List<String> symbols) {
            this.symbols = symbols;
        }
    }

    public static class Crawl {
        private Historical historical;
        private Schedule schedule;

        public static class Historical {
            private String startDate;
            private String interval;

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public String getInterval() {
                return interval;
            }

            public void setInterval(String interval) {
                this.interval = interval;
            }
        }

        public static class Schedule {
            private String dailyQuote;
            private String weeklyRecommendation;
            private String hourlyNews;

            public String getDailyQuote() {
                return dailyQuote;
            }

            public void setDailyQuote(String dailyQuote) {
                this.dailyQuote = dailyQuote;
            }

            public String getWeeklyRecommendation() {
                return weeklyRecommendation;
            }

            public void setWeeklyRecommendation(String weeklyRecommendation) {
                this.weeklyRecommendation = weeklyRecommendation;
            }

            public String getHourlyNews() {
                return hourlyNews;
            }

            public void setHourlyNews(String hourlyNews) {
                this.hourlyNews = hourlyNews;
            }
        }

        public Historical getHistorical() {
            return historical;
        }

        public void setHistorical(Historical historical) {
            this.historical = historical;
        }

        public Schedule getSchedule() {
            return schedule;
        }

        public void setSchedule(Schedule schedule) {
            this.schedule = schedule;
        }
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Crawl getCrawl() {
        return crawl;
    }

    public void setCrawl(Crawl crawl) {
        this.crawl = crawl;
    }
}
