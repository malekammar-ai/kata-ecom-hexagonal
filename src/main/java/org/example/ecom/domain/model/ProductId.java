package org.example.ecom.domain.model;


//DDD
// Value Object
// ProductId is a value object that represents the unique identifier of a product in the e-commerce domain.
public record ProductId(String value) {
    public ProductId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("L'identifiant produit ne peut pas être vide");
        }
        if (!value.startsWith("PROD-")) {
            throw new IllegalArgumentException("Un identifiant produit doit commencer par 'PROD-'");
        }
    }
}