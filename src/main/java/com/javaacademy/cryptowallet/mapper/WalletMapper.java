package com.javaacademy.cryptowallet.mapper;

import com.javaacademy.cryptowallet.dto.CreateWalletRequestDto;
import com.javaacademy.cryptowallet.dto.WalletDto;
import com.javaacademy.cryptowallet.entity.Wallet;
import com.javaacademy.cryptowallet.entity.enums.Currency;
import com.javaacademy.cryptowallet.exception.currency.CurrencyDoesNotSupportException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class WalletMapper {
    private static final String CURRENCY_DOES_NOT_SUPPORT = "Указанная валюта не поддерживается";

    public Wallet requestToWallet(CreateWalletRequestDto dto) {
        Currency currency;
        try {
            currency = Currency.valueOf(dto.getCurrency());
        } catch (IllegalArgumentException e) {
            throw new CurrencyDoesNotSupportException(CURRENCY_DOES_NOT_SUPPORT);
        }
        return new Wallet(dto.getUserName(), currency, BigDecimal.ZERO, UUID.randomUUID());
    }

    public WalletDto toDto(Wallet wallet) {
        return new WalletDto(wallet.getUserLogin(), wallet.getCurrency(), wallet.getAmount(), wallet.getUuid());
    }
}
