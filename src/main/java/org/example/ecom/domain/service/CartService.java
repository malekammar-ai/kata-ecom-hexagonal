package org.example.ecom.domain.service;

import org.example.ecom.domain.port.in.CartUseCase;
import org.example.ecom.domain.port.out.CatalogPort;
import org.example.ecom.domain.exception.UnknownProductException;
import org.example.ecom.domain.model.Cart;
import org.example.ecom.domain.model.CartLine;
import org.example.ecom.domain.model.Product;
import org.example.ecom.domain.model.ProductId;
import org.example.ecom.domain.model.pricing.PricingResult;
import org.example.ecom.domain.model.pricing.PricingService;

import java.util.List;

//Domain porte les règles métier, tandis que l'Application orchestre les cas d'utilisation.
public class CartService implements CartUseCase {


    private final CatalogPort catalog;
    private final Cart cart;
    private final PricingService pricing;

    public CartService(CatalogPort catalog, Cart cart, PricingService pricing) {
        this.catalog = catalog;
        this.cart = cart;
        this.pricing = pricing;
    }

    @Override
    public void addProduct(ProductId id, int quantity) {
        catalog.findById(id).orElseThrow(() -> new UnknownProductException(id));
        cart.addProduct(id, quantity);
    }

    @Override
    public void removeProduct(ProductId id, int quantity) {
        cart.removeProduct(id, quantity);
    }

    @Override
    public PricingResult summary() {
        List<CartLine> lines = cart.getItems().entrySet().stream()
                .map(e -> new CartLine(findProduct(e.getKey()), e.getValue()))
                .toList();
        return pricing.price(lines);
    }

    private Product findProduct(ProductId id) {
        return catalog.findById(id).orElseThrow(() -> new UnknownProductException(id));
    }
}