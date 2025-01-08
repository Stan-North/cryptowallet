package com.javaacademy.cryptowallet.service.impls;

import com.javaacademy.cryptowallet.service.RubleConverterService;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
@Profile("prod")
public class RubleConverterServiceImpl implements RubleConverterService {
    private final OkHttpClient client;
    @Value("${api.cbr.endpoint}")
    private String endpoint;
    private static final String PATH_TO_RATE = "$.rates.USD";

    @Override
    public BigDecimal toRub(BigDecimal usdAmount) {
        BigDecimal value = makeRequest(usdAmount);
        return usdAmount.divide(value, RoundingMode.DOWN);
    }

    @Override
    public BigDecimal toUsd(BigDecimal rubAmount) {
        BigDecimal value = makeRequest(rubAmount);
        return rubAmount.multiply(value);
    }

    @SneakyThrows
    private BigDecimal makeRequest(BigDecimal currencyAmount) {
        Request request = new Request.Builder()
                .get()
                .url(endpoint)
                .build();
        Response response = client.newCall(request).execute();
        return JsonPath.parse(response.body().string()).read(JsonPath.compile(PATH_TO_RATE), BigDecimal.class);
    }
}
