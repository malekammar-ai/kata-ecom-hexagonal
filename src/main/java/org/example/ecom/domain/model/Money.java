package org.example.ecom.domain.model;

import java.math.BigDecimal;

/*
* La classe Money sert à représenter et manipuler de l'argent de manière fiable,
* sans disperser les règles de calcul monétaire dans tout ton projet.
*/
public record Money(BigDecimal amount) {
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant ne peut pas être négatif");
        }
    }

    public static  Money of(String value){
        return new Money(new BigDecimal(value));
    }

    public Money multiply(int factor) {
        return new Money(amount.multiply(BigDecimal.valueOf(factor)));
    }

    public Money subtract(Money other) {
        BigDecimal result = amount.subtract(other.amount());
        return new Money(result.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : result);
    }

    public Money add(Money other) {
        return new Money(amount.add(other.amount));
    }
}