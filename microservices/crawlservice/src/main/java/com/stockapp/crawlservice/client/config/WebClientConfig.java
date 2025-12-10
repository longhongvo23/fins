package com.stockapp.crawlservice.client.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    private final ApiProperties apiProperties;

    public WebClientConfig(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    private HttpClient createHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofSeconds(30))
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(30, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(30, TimeUnit.SECONDS)));
    }

    private ExchangeStrategies createExchangeStrategies() {
        return ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB
                .build();
    }

    @Bean(name = "twelveDataWebClient")
    public WebClient twelveDataWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiProperties.getApi().getTwelveData().getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(createHttpClient()))
                .exchangeStrategies(createExchangeStrategies())
                .build();
    }

    @Bean(name = "finnhubWebClient")
    public WebClient finnhubWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiProperties.getApi().getFinnhub().getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(createHttpClient()))
                .exchangeStrategies(createExchangeStrategies())
                .build();
    }

    @Bean(name = "marketauxWebClient")
    public WebClient marketauxWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiProperties.getApi().getMarketaux().getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(createHttpClient()))
                .exchangeStrategies(createExchangeStrategies())
                .build();
    }
}
