package com.serjnn.OrderDetailsService.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderDetails(
        long id,
        UUID uuid,
        long clientId,
        String productsIds,
        BigDecimal sum,
        LocalDateTime createdAt
) {
    public OrderDetails(UUID uuid, long clientId, String productsIds, BigDecimal sum) {
        this(0, uuid, clientId, productsIds, sum, LocalDateTime.now());
    }
}
