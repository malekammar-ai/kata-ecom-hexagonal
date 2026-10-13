package org.example.ecom.infrastructure.adapter.out;

import org.example.ecom.domain.port.out.CatalogPort;
import org.example.ecom.domain.model.Product;
import org.example.ecom.domain.model.ProductId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryCatalog implements CatalogPort {

    private final Map<ProductId, Product> products = new HashMap<>();

    public InMemoryCatalog with(Product p) { products.put(p.id(), p); return this; }

    @Override
    public Optional<Product> findById(ProductId id) {
        return Optional.ofNullable(products.get(id));
    }
}