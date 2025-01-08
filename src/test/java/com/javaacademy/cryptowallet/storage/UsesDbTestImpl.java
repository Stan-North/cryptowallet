package com.javaacademy.cryptowallet.storage;

import com.javaacademy.cryptowallet.data_base.UsersDb;
import com.javaacademy.cryptowallet.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UsesDbTestImpl implements UsersDb {
    private final Map<String, User> data = new HashMap<>();

    @PostConstruct
    public void init() {
        User user = new User("login", "example@gmail.com", "12345");
        data.put("login", user);
    }

    @Override
    public Map<String, User> getData() {
        return data;
    }
}
