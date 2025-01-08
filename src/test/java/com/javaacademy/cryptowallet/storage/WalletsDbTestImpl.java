package com.javaacademy.cryptowallet.storage;

import com.javaacademy.cryptowallet.data_base.WalletsDb;
import com.javaacademy.cryptowallet.entity.Wallet;
import com.javaacademy.cryptowallet.entity.enums.Currency;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class WalletsDbTestImpl implements WalletsDb {
    private final Map<UUID, Wallet> data = new HashMap<>();

    @Override
    public Map<UUID, Wallet> getData() {
        return data;
    }
}
