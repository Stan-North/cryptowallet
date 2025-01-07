package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class WalletActionDto {
    @JsonProperty("account_id")
    private UUID accountId;
    @JsonProperty("rubles_amount")
    private BigDecimal rublesAmount;
}
