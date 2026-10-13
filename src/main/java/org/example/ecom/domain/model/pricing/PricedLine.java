package org.example.ecom.domain.model.pricing;

import org.example.ecom.domain.model.CartLine;
import org.example.ecom.domain.model.Money;

public record PricedLine(CartLine line, Money total) {}
