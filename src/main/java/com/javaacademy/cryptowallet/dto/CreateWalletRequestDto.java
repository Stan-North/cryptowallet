package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Schema(description = "DTO для создания криптокошелька")
public class CreateWalletRequestDto {
    @JsonProperty("username")
    @Schema(description = "логин пользователя")
    private final String userName;
    @JsonProperty("crypto_type")
    @Schema(description = "Валюта кошелька")
    private final String currency;
}
