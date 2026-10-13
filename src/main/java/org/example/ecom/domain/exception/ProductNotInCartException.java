package org.example.ecom.domain.exception;

import org.example.ecom.domain.model.ProductId;

public class ProductNotInCartException extends RuntimeException {
    public ProductNotInCartException(ProductId id) {
        super("Produit absent du panier : " + id.value());
    }
}