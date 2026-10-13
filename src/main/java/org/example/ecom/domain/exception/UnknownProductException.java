package org.example.ecom.domain.exception;

import org.example.ecom.domain.model.ProductId;

public class UnknownProductException extends RuntimeException {
    public UnknownProductException(ProductId id) {
        super("Produit inconnu du catalogue : " + id.value());
    }
}