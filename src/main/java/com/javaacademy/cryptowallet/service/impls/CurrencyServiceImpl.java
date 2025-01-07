package com.javaacademy.cryptowallet.service.impls;

import com.javaacademy.cryptowallet.entity.enums.Currency;
import com.javaacademy.cryptowallet.exception.currency.ResponseException;
import com.javaacademy.cryptowallet.service.CurrencyService;
import com.javaacademy.cryptowallet.util.HttpClient;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Profile("prod")
public class CurrencyServiceImpl implements CurrencyService {
    private final HttpClient client;
    @Value("${api.coingecko.url}")
    private String url;
    @Value("${api.coingecko.header}")
    private String header;
    @Value("${api.coingecko.token}")
    private String token;
    private static final String CURRENCY_PATH_USD = "$['%s']['usd']";
    private static final String ENDPOINT_USD = "/simple/price?ids=%s&vs_currencies=usd";
    private static final String RESPONSE_EXCEPTION_MESSAGE = "no response received or response body is empty";

    @SuppressWarnings("checkstyle:EmptyBlock")
    @Override
    @SneakyThrows
    public BigDecimal getUsdPrice(Currency currency) {
        Request request = createRequest(currency);
        Response response = client.getClient().newCall(request).execute();
        if (!response.isSuccessful() || response.body() == null) {
            throw new ResponseException(RESPONSE_EXCEPTION_MESSAGE);
        }
        return convertToBigDecimal(response, currency);
    }

    private Request createRequest(Currency currency) {
        return new Request.Builder()
                .get()
                .url(url + String.format(ENDPOINT_USD, currency.getFullName()))
                .addHeader(header, token)
                .build();
    }

    @SneakyThrows
    private BigDecimal convertToBigDecimal(Response response, Currency currency) {
        String path = String.format(CURRENCY_PATH_USD, currency.getFullName());
        return JsonPath.parse(response.body().string()).read(JsonPath.compile(path), BigDecimal.class);
    }
}
