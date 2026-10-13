package org.example.ecom.domain;

import org.example.ecom.domain.model.CartLine;
import org.example.ecom.domain.model.Money;
import org.example.ecom.domain.model.Product;
import org.example.ecom.domain.model.ProductId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartLineTest {
    static final ProductId JACKET   = new ProductId("PROD-003");

    @Test
    void shouldCalculateFullPrice() {
        CartLine line = new CartLine(new Product(JACKET,"JACKET", Money.of("50.00")), 3);

        assertEquals(
                Money.of("150.00"),
                line.fullPrice()
        );
    }
}
