package com.javaacademy.cryptowallet.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class HttpClient {
    private final OkHttpClient client = new OkHttpClient();
}
