package com.bcbsm.inventory;

import java.math.BigDecimal;

/**
 * Local view of the pricing-service contract (consumer side).
 *
 * @param bookId       the book identifier
 * @param basePrice    list price before discount
 * @param currency     ISO currency code
 * @param discountTier tier this service applies to compute effective price
 */
public record PriceView(
        String bookId,
        BigDecimal basePrice,
        String currency,
        String discountTier
) {
}
