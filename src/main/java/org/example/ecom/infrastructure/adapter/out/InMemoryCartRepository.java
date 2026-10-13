package org.example.ecom.infrastructure.adapter.out;

import org.example.ecom.application.port.out.CartRepository;
import org.example.ecom.domain.model.Cart;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCartRepository implements CartRepository {

    private Cart cart = new Cart();

    @Override
    public synchronized Cart getCart() {
        return cart;
    }

    @Override
    public synchronized void save(Cart cart) {
        this.cart = cart;
    }
}