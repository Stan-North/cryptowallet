package com.javaacademy.cryptowallet.service.impls;

import com.javaacademy.cryptowallet.service.RubleConverterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Profile("local")
public class RubleConverterServiceMock implements RubleConverterService {
    @Value("${exchange.rate}")
    private BigDecimal rate;

    @Override
    public BigDecimal toRub(BigDecimal usdAmount) {
        return usdAmount.divide(rate, RoundingMode.DOWN);
    }

    @Override
    public BigDecimal toUsd(BigDecimal rubAmount) {
        return rubAmount.multiply(rate);
    }
}
