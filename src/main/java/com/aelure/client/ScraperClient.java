package com.aelure.client;

import com.aelure.model.FashionItem;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Component
public class ScraperClient {

    private final WebClient webClient;

    public ScraperClient(WebClient.Builder builder) {
        this.webcleint = builder.baseUrl("http://localhost:8080").build();
    }

    public List<FashionItem> scrapeFashionSites(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/scrape").queryParam("q", query).build())
                .retrieve()
                .bodyToFlux(FashionItem.class)
                .collectList()
                .block();
    }
}