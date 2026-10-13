package org.example.ecom.domain.model.pricing;

import org.example.ecom.domain.model.CartLine;
import org.example.ecom.domain.model.Money;

import java.util.List;

public class PricingService {

    public PricingResult price(List<CartLine> lines) {
        List<PricedLine> priced = lines.stream().map(this::priceLine).toList();

        Money total = priced.stream()
                .map(PricedLine::total)
                .reduce(Money.ZERO, Money::add);

        return new PricingResult(priced, total, total);
    }

    private PricedLine priceLine(CartLine line) {
        Money total = line.fullPrice();
        return new PricedLine(line, total);
    }
}
