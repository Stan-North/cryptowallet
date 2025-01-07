package com.javaacademy.cryptowallet.entity;

import com.javaacademy.cryptowallet.entity.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    private String userLogin;
    private Currency currency;
    @Setter
    private BigDecimal amount;
    private UUID uuid;
}
