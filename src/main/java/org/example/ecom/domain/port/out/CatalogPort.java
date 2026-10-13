package org.example.ecom.domain.port.out;

import org.example.ecom.domain.model.Product;
import org.example.ecom.domain.model.ProductId;

import java.util.Optional;

// port SORTANT : le métier a BESOIN d'un catalogue
public interface CatalogPort {
    Optional<Product> findById(ProductId id);
}