package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateWalletRequestDto {
    @JsonProperty("username")
    private String userName;
    @JsonProperty("crypto_type")
    private String currency;
}
