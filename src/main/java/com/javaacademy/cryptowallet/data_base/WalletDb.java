package com.javaacademy.cryptowallet.data_base;

import com.javaacademy.cryptowallet.entity.Wallet;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Getter
public class WalletDb {
    private final Map<UUID, Wallet> data = new HashMap<>();
}
