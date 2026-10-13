package org.example.ecom.domain;

import org.example.ecom.domain.exception.InvalidQuantityException;
import org.example.ecom.domain.model.Cart;
import org.example.ecom.domain.model.ProductId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    static final ProductId TSHIRT = new ProductId("PROD-001");
    static final ProductId JEAN   = new ProductId("PROD-002");

     Cart cart = new Cart();

    @Test
    void ajoute_plusieurs_exemplaires_du_meme_produit() {
        cart.addProduct(TSHIRT, 2);
        cart.addProduct(TSHIRT, 1);
        assertEquals(3, cart.getItems().get(TSHIRT));
    }

    @Test void retire_un_exemplaire() {
        cart.addProduct(TSHIRT, 2);
        cart.removeProduct(TSHIRT, 1);
        assertEquals(1, cart.getItems().get(TSHIRT));
    }

    @Test void supprime_la_ligne_quand_la_quantite_atteint_zero() {
        cart.addProduct(TSHIRT, 1);
        cart.removeProduct(TSHIRT, 1);
        assertFalse(cart.getItems().containsKey(TSHIRT));
        assertTrue(cart.isEmpty());
    }

    @Test void refuse_de_retirer_plus_que_la_quantite_presente() {
        cart.addProduct(TSHIRT, 1);
        assertThrows(InvalidQuantityException.class, () -> cart.removeProduct(TSHIRT, 2));
    }

    @Test void refuse_une_quantite_nulle_ou_negative() {
        assertThrows(InvalidQuantityException.class, () -> cart.addProduct(TSHIRT, 0));
        assertThrows(InvalidQuantityException.class, () -> cart.addProduct(JEAN, -1));
    }
}
