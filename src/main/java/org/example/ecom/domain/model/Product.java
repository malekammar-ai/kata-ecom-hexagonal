package org.example.ecom.domain.model;

public record Product(
        ProductId id,
        String name,
        Money unitPrice
) {
}