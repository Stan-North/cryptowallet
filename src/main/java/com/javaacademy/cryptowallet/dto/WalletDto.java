package com.javaacademy.cryptowallet.dto;

import com.javaacademy.cryptowallet.entity.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Schema(description = "DTO для кошелька")
public class WalletDto {
    @Schema(description = "логин пользователя")
    private final String userLogin;
    @Schema(description = "валюта счета")
    private final Currency currency;
    @Schema(description = "сумма на счету")
    private final BigDecimal amount;
    @Schema(description = "id счета")
    private final UUID uuid;
}
