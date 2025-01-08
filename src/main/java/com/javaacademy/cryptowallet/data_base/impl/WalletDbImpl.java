package com.javaacademy.cryptowallet.data_base.impl;

import com.javaacademy.cryptowallet.data_base.WalletsDb;
import com.javaacademy.cryptowallet.entity.Wallet;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Profile("prod")
public class WalletDbImpl implements WalletsDb {
    private final Map<UUID, Wallet> data = new HashMap<>();

    @Override
    public Map<UUID, Wallet> getData() {
        return data;
    }
}
