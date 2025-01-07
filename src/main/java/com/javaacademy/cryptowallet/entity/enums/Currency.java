package com.javaacademy.cryptowallet.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Currency {
    BTC("bitcoin"),
    ETH("ethereum"),
    SOL("solana");

    private final String fullName;
}
