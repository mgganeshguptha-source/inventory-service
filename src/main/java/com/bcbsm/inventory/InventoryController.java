package com.bcbsm.inventory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final PricingClient pricingClient;

    // Static stock levels. B001 in stock, B002 in stock, B003 out of stock.
    private static final Map<String, Integer> STOCK = Map.of(
            "B001", 12,
            "B002", 3,
            "B003", 0
    );

    // Discount applied per tier when computing effective price.
    private static final Map<String, BigDecimal> TIER_DISCOUNT = Map.of(
            "STANDARD", new BigDecimal("0.00"),
            "PREMIUM", new BigDecimal("0.10")
    );

    public InventoryController(PricingClient pricingClient) {
        this.pricingClient = pricingClient;
    }

    @GetMapping("/{bookId}")
    public Mono<ResponseEntity<Inventory>> getInventory(@PathVariable String bookId) {
        Integer stock = STOCK.get(bookId);
        if (stock == null) {
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        return pricingClient.getPrice(bookId)
                .map(price -> {
                    BigDecimal discount = TIER_DISCOUNT.getOrDefault(price.discountTier(), BigDecimal.ZERO);
                    BigDecimal effective = price.basePrice()
                            .multiply(BigDecimal.ONE.subtract(discount))
                            .setScale(2, RoundingMode.HALF_UP);
                    Inventory inv = new Inventory(
                            bookId,
                            stock,
                            stock > 0,
                            effective,
                            price.currency());
                    return ResponseEntity.ok(inv);
                })
                .onErrorResume(WebClientResponseException.NotFound.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }
}
