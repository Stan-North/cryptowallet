package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Schema(description = "DTO для действий с криптосчетом")
public class WalletActionDto {
    @JsonProperty("account_id")
    @Schema(description = "id счета")
    private final UUID accountId;
    @JsonProperty("rubles_amount")
    @Schema(description = "сумма в рублях")
    private final BigDecimal rublesAmount;
}
