package com.javaacademy.cryptowallet.service.impls;

import com.javaacademy.cryptowallet.entity.enums.Currency;
import com.javaacademy.cryptowallet.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Profile("local")
public class CurrencyServiceMock implements CurrencyService {
    @Value("${currency.price}")
    private BigDecimal price;
    @Override
    public BigDecimal getUsdPrice(Currency currency) {
        return price;
    }
}
