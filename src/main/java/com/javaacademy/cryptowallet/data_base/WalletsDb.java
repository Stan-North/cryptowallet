package com.javaacademy.cryptowallet.data_base;

import com.javaacademy.cryptowallet.entity.Wallet;

import java.util.Map;
import java.util.UUID;

public interface WalletsDb {

    Map<UUID, Wallet> getData();
}
