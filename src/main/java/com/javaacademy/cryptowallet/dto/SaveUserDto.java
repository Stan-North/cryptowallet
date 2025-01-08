package com.javaacademy.cryptowallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Schema(description = "DTO для сброса пароля")
public class SaveUserDto {
    @Schema(description = "логин пользователя")
    private final String login;
    @Schema(description = "email пользователя")
    private final String email;
    @Schema(description = "пароль пользователя")
    private final String password;
}
