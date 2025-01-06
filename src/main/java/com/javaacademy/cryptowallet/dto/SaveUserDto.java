package com.javaacademy.cryptowallet.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SaveUserDto {
    private final String login;
    private final String email;
    private final String password;
}
