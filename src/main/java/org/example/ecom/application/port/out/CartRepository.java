package org.example.ecom.application.port.out;

import org.example.ecom.domain.model.Cart;

public interface CartRepository {
    Cart getCart();
    void save(Cart cart);
}