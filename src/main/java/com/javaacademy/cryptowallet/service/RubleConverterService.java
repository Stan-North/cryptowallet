package com.javaacademy.cryptowallet.service;

import java.math.BigDecimal;

public interface RubleConverterService {
    BigDecimal toRub(BigDecimal usdAmount);
    BigDecimal toUsd(BigDecimal rubAmount);
}
