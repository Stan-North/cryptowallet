package com.javaacademy.cryptowallet.dto;

import com.javaacademy.cryptowallet.entity.enums.Currency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class WalletDto {
    private final Currency currency;
    private final BigDecimal amount;
    private UUID uuid;
}
