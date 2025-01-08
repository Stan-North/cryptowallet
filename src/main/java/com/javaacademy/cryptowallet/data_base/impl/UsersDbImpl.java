package com.javaacademy.cryptowallet.data_base.impl;

import com.javaacademy.cryptowallet.data_base.UsersDb;
import com.javaacademy.cryptowallet.entity.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Profile("prod")
public class UsersDbImpl implements UsersDb {
    private final Map<String, User> data = new HashMap<>();

    @Override
    public Map<String, User> getData() {
        return data;
    }

}

