package com.example.contract.doamin;

import com.example.contract.doamin.embeddable.ProductTerm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    @DisplayName("총 보험료 계산 테스트 케이스 ok case")
    public void calculatePremium_ok() {

        Product mock = Product.createBuilder()
                .title("여행자 보험")
                .term(new ProductTerm(1, 3))
                .warrants(getWarrants())
                .build();

        BigDecimal n = mock.calculatePremium();

        assertEquals(new BigDecimal(20000), n);
    }

    @Test
    @DisplayName("총 보험료 계산 term 이 null ")
    public void calculatePremium_fail1() {

        Product mock = Product.createBuilder()
                .title("여행자 보험")
                .warrants(getWarrants())
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            BigDecimal n = mock.calculatePremium();
        });

    }

    @Test
    @DisplayName("총 보험료 계산 테스트 담보 데이터가 null")
    public void calculatePremium_fail2() {

        Product mock = Product.createBuilder()
                .title("여행자 보험")
                .term(new ProductTerm(1, 3))
                .build();

        BigDecimal n = mock.calculatePremium();

        assertEquals(BigDecimal.ZERO, n);
    }


    private Set<Warrant> getWarrants() {
        Set<Warrant> mock = new HashSet<>();

        Warrant warrant = Warrant.createBuilder()
                .title("상해치료")
                .subscriptionAmount(new BigDecimal(1000000))
                .standardAmount(new BigDecimal(100))
                .build();

        mock.add(warrant);

        return mock;
    }
}
