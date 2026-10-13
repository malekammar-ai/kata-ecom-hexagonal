package org.example.ecom.domain.model;

import org.example.ecom.domain.exception.InvalidQuantityException;
import org.example.ecom.domain.exception.ProductNotInCartException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private final Map<ProductId, Integer> items = new HashMap<>();

    public void addProduct(ProductId productId, int quantity) {
        if (quantity <= 0) throw new InvalidQuantityException("Quantité invalide");
        items.merge(productId, quantity, Integer::sum);
    }

    public void removeProduct(ProductId productId, int quantity) {
        if (quantity <= 0)
            throw new InvalidQuantityException("La quantité à ajouter doit être > 0");

        Integer current = items.get(productId);
        if (current == null)
            throw new ProductNotInCartException(productId);

        if (quantity > current)
            throw new InvalidQuantityException(
                    "Impossible de retirer " + quantity + " : seulement " + current + " dans le panier");

        int remaining = current - quantity;
        if (remaining == 0)
            items.remove(productId);
        else
            items.put(productId, remaining);
    }

    public Map<ProductId, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public boolean isEmpty() { return items.isEmpty(); }


}
