package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "DTO для сброса пароля")
public class ResetPassRequestDto {
    @Schema(description = "логин пользователя")
    private final String login;
    @JsonProperty("old_password")
    @Schema(description = "старый пароль")
    private final String oldPassword;
    @JsonProperty("new_password")
    @Schema(description = "новый пароль")
    private final String newPassword;
}
