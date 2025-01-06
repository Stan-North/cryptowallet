package com.javaacademy.cryptowallet.data_base;

import com.javaacademy.cryptowallet.entity.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class UsersDb {
    private final Map<String, User> data = new HashMap<>();
}
