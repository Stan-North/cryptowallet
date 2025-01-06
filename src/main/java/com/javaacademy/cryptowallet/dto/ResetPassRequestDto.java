package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResetPassRequestDto {
    private final String login;
    @JsonProperty("old_password")
    private final String oldPassword;
    @JsonProperty("new_password")
    private final String newPassword;
}
