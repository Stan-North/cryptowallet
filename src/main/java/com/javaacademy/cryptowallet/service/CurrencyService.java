package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.entity.enums.Currency;

import java.math.BigDecimal;

public interface CurrencyService {
    BigDecimal getUsdPrice(Currency currency);
}
