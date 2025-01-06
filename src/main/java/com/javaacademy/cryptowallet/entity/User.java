package com.javaacademy.cryptowallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private final String login;
    private final String email;
    private String password;
}
