package com.bcbsm.inventory;

import java.math.BigDecimal;

/**
 * Inventory contract for a book: stock plus effective price after applying the
 * discountTier received from pricing-service.
 *
 * @param bookId         the book identifier
 * @param stock          units in stock
 * @param available      true when stock &gt; 0
 * @param effectivePrice price after tier discount
 * @param currency       ISO currency code
 */
public record Inventory(
        String bookId,
        int stock,
        boolean available,
        BigDecimal effectivePrice,
        String currency
) {
}
