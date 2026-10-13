package org.example.ecom.application.dto;

public record CartResponse(
        List<CartLineResponse> lines,
        BigDecimal total
) {
    public CartResponse {
        lines = List.copyOf(lines);
    }
}