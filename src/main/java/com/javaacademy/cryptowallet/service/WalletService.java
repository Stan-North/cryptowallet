package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.dto.CreateWalletRequestDto;
import com.javaacademy.cryptowallet.dto.WalletDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface WalletService {
    WalletDto findByUuid(UUID uuid);
    List<WalletDto> findAll(String login);
    UUID save(CreateWalletRequestDto dto);
    void deposit(UUID uuid, BigDecimal rubAmount);
    String withdraw(UUID uuid, BigDecimal rubAmount);
    BigDecimal showWalletBalance(UUID uuid);
    BigDecimal showAllWalletBalance(String login);
}
