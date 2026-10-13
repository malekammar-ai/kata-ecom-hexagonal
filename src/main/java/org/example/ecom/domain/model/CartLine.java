package org.example.ecom.domain.model;

import org.example.ecom.domain.exception.InvalidQuantityException;

import java.util.Objects;

public record CartLine(Product product, int quantity) {
    public CartLine {
        Objects.requireNonNull(product);
        if (quantity <= 0)
            throw new InvalidQuantityException("Une ligne doit avoir une quantité > 0");
    }

    public Money fullPrice() {
        return product.unitPrice().multiply(quantity);
    }
}