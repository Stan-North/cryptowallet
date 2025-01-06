package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.dto.ResetPassRequestDto;
import com.javaacademy.cryptowallet.dto.SaveUserDto;
import com.javaacademy.cryptowallet.entity.User;

public interface UserService {
    void save(SaveUserDto saveUserDto);
    User getByLogin(String login);
    void resetPassword(ResetPassRequestDto dto);
}
