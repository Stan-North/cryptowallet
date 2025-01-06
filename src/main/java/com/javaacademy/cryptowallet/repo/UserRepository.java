package com.javaacademy.cryptowallet.repo;

import com.javaacademy.cryptowallet.dto.SaveUserDto;
import com.javaacademy.cryptowallet.entity.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByLogin(String login);

    void save(SaveUserDto saveUserDto);
}
