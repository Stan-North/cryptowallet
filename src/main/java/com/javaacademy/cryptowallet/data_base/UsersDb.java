package com.javaacademy.cryptowallet.data_base;

import com.javaacademy.cryptowallet.entity.User;
import java.util.Map;

public interface UsersDb {

    Map<String, User> getData();
}
