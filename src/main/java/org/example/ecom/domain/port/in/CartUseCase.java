package org.example.ecom.domain.port.in;

import org.example.ecom.domain.model.ProductId;
import org.example.ecom.domain.model.pricing.PricingResult;

//port ENTRANT : ce que l'extérieur peut demander
public interface CartUseCase {
    void addProduct(ProductId id, int quantity);
    void removeProduct(ProductId id, int quantity);
    PricingResult summary();
}