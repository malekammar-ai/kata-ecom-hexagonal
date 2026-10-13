package org.example.ecom.application.dto;

import java.math.BigDecimal;

public record CartLineResponse(
        String productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {}