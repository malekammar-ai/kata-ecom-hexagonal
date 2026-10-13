package org.example.ecom.service;

import org.example.ecom.domain.service.CartService;
import org.example.ecom.domain.exception.UnknownProductException;
import org.example.ecom.domain.model.Cart;
import org.example.ecom.domain.model.Money;
import org.example.ecom.domain.model.Product;
import org.example.ecom.domain.model.ProductId;
import org.example.ecom.domain.model.pricing.PricingService;
import org.example.ecom.infrastructure.adapter.out.InMemoryCatalog;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    static final ProductId TSHIRT = new ProductId("PROD-001");
    static final ProductId JEAN   = new ProductId("PROD-002");

    InMemoryCatalog catalog = new InMemoryCatalog()
            .with(new Product(TSHIRT, "T-shirt", Money.of("25")))
            .with(new Product(JEAN, "Jean", Money.of("60")));
    Cart cart = new Cart();
    CartService service = new CartService(catalog, cart,
            new PricingService(List.of()));

    @Test void scenario_de_l_enonce() {
        service.addProduct(TSHIRT, 2);
        service.addProduct(JEAN, 1);
        assertEquals(Money.of("110"), service.summary().total());

        service.removeProduct(TSHIRT, 1);
        assertEquals(Money.of("85"), service.summary().total());

        service.removeProduct(TSHIRT, 1);
        var result = service.summary();
        assertEquals(1, result.lines().size());            // le T-shirt a disparu
        assertEquals(Money.of("60"), result.total());
    }

    @Test void refuse_un_produit_absent_du_catalogue() {
        assertThrows(UnknownProductException.class,
                () -> service.addProduct(new ProductId("PROD-999"), 1));
        assertTrue(cart.isEmpty());
    }
}