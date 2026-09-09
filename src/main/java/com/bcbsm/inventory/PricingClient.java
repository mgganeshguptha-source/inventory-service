package com.bcbsm.inventory;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PricingClient {

    private final WebClient pricingWebClient;

    public PricingClient(WebClient pricingWebClient) {
        this.pricingWebClient = pricingWebClient;
    }

    public Mono<PriceView> getPrice(String bookId) {
        return pricingWebClient.get()
                .uri("/pricing/{bookId}", bookId)
                .retrieve()
                .bodyToMono(PriceView.class);
    }
}
