package org.example.ecom.domain.model.pricing;

import org.example.ecom.domain.model.Money;

import java.util.List;

public record PricingResult(List<PricedLine> lines, Money subtotal, Money total) {
    public PricingResult {
        lines = List.copyOf(lines);
    }
}