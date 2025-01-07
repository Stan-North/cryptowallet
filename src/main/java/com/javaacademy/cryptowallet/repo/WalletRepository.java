package com.javaacademy.cryptowallet.repo;

import com.javaacademy.cryptowallet.entity.Wallet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {
        void save(Wallet wallet);
        Optional<Wallet> findByID(UUID uuid);
        Optional<List<Wallet>> findAll(String login);
}
