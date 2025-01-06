package com.javaacademy.cryptowallet.mapper;

import com.javaacademy.cryptowallet.dto.SaveUserDto;
import com.javaacademy.cryptowallet.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(SaveUserDto saveUserDto) {
        return new User(
                saveUserDto.getLogin(),
                saveUserDto.getEmail(),
                saveUserDto.getPassword());
    }
}
