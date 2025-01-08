package com.javaacademy.cryptowallet.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }
}
