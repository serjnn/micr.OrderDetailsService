package com.serjnn.OrderDetailsService.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderDetails(
        Long id,
        UUID uuid,
        Long clientId,
        String productsIds,
        BigDecimal sum,
        LocalDateTime createdAt
) {

}
