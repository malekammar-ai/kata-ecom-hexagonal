package org.example.ecom.service;

import org.example.ecom.domain.model.CartLine;
import org.example.ecom.domain.model.Money;
import org.example.ecom.domain.model.Product;
import org.example.ecom.domain.model.ProductId;
import org.example.ecom.domain.model.pricing.PricingService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingServiceTest {

    static final Product TSHIRT = new Product(new ProductId("PROD-001"), "T-shirt", Money.of("25"));
    static final Product JEAN   = new Product(new ProductId("PROD-002"), "Jean", Money.of("60"));

    PricingService noRulesCart = new PricingService();

    @Test void panier_vide_vaut_zero() {
        assertEquals(Money.ZERO, noRulesCart.price(List.of()).total());
    }

    @Test void exemple_de_l_enonce_110_euros() {
        var result = noRulesCart.price(List.of(new CartLine(TSHIRT, 2), new CartLine(JEAN, 1)));

        assertEquals(Money.of("50"), result.lines().get(0).total());
        assertEquals(Money.of("60"), result.lines().get(1).total());
        assertEquals(Money.of("110"), result.total());
    }
}