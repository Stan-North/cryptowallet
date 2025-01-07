package com.javaacademy.cryptowallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String login;
    private String email;
    private String password;
    private final List<Wallet> wallets = new ArrayList<>();
}
