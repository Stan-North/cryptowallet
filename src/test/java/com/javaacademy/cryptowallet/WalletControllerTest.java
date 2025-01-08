package com.javaacademy.cryptowallet;

import com.javaacademy.cryptowallet.data_base.UsersDb;
import com.javaacademy.cryptowallet.data_base.WalletsDb;
import com.javaacademy.cryptowallet.dto.CreateWalletRequestDto;
import com.javaacademy.cryptowallet.dto.WalletActionDto;
import com.javaacademy.cryptowallet.dto.WalletDto;
import com.javaacademy.cryptowallet.entity.Wallet;
import com.javaacademy.cryptowallet.entity.enums.Currency;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("local")
public class WalletControllerTest {
    private static final Logger log = LoggerFactory.getLogger(WalletControllerTest.class);
    @Autowired
    private WalletsDb walletsDb;
    @Autowired
    private UsersDb usersDb;

    @Test
    @DisplayName("Создание криптосчета")
    void walletCreateSuccess() {
        CreateWalletRequestDto dto = new CreateWalletRequestDto("login", "SOL");
        UUID uuid = RestAssured.given()
                .port(8008)
                .contentType(ContentType.JSON)
                .body(dto)
                .log().all()
                .post("/cryptowallet")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body()
                .as(UUID.class);
        Assertions.assertEquals(1, walletsDb.getData().size());
        Wallet wallet = walletsDb.getData().get(uuid);
        Assertions.assertEquals("login", wallet.getUserLogin());
        Assertions.assertEquals(Currency.SOL, wallet.getCurrency());
        Assertions.assertEquals(BigDecimal.ZERO, wallet.getAmount());
    }

    @Test
    @DisplayName("Получения списка счетов")
    void getAllWalletsSuccess() {
        UUID uuid = UUID.fromString("f0c92175-de9d-44d4-a1d8-dfcc8eccc40f");
        Wallet wallet = new Wallet("login", Currency.BTC, BigDecimal.valueOf(10000), uuid);
        usersDb.getData().get("login").getWallets().add(wallet);
        List<WalletDto> dtos = RestAssured.given()
                .port(8008)
                .contentType(ContentType.JSON)
                .queryParam("username", "login")
                .log().all()
                .get("/cryptowallet")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        log.info(usersDb.getData().values().toString());

        Assertions.assertEquals(1, dtos.size());
        WalletDto walletDto = dtos.get(0);
        Assertions.assertEquals(BigDecimal.valueOf(10000), walletDto.getAmount());
        Assertions.assertEquals("login", walletDto.getUserLogin());
        Assertions.assertEquals(Currency.BTC, walletDto.getCurrency());
        Assertions.assertEquals(uuid, walletDto.getUuid());
    }

    @Test
    @DisplayName("Пополнение счета")
    void refillSuccess() {
        UUID uuid = UUID.fromString("5c480ca5-2f96-4253-b59d-ad3bdc5d7218");
        Wallet wallet = new Wallet("login", Currency.BTC, BigDecimal.valueOf(10000), uuid);
        walletsDb.getData().put(uuid, wallet);
        usersDb.getData().get("login").getWallets().add(wallet);
        WalletActionDto dto = new WalletActionDto(uuid, BigDecimal.valueOf(50000));
        RestAssured.given()
                .port(8008)
                .contentType(ContentType.JSON)
                .body(dto)
                .log().all()
                .post("/cryptowallet/refill")
                .then()
                .log().all()
                .statusCode(200);
        Assertions.assertEquals(BigDecimal.valueOf(10500), wallet.getAmount());
    }

    @Test
    @DisplayName("Снятие денег со счета")
    void withdrawalSuccess() {
        UUID uuid = UUID.fromString("5c480ca5-2f96-4253-b59d-ad3bdc5d7218");
        Wallet wallet = new Wallet("login", Currency.BTC, BigDecimal.valueOf(10000), uuid);
        walletsDb.getData().put(uuid, wallet);
        usersDb.getData().get("login").getWallets().add(wallet);
        WalletActionDto dto = new WalletActionDto(uuid, BigDecimal.valueOf(50000));
        RestAssured.given()
                .port(8008)
                .contentType(ContentType.JSON)
                .body(dto)
                .log().all()
                .post("/cryptowallet/withdrawal")
                .then()
                .log().all()
                .statusCode(200);
        Assertions.assertEquals(BigDecimal.valueOf(9500), wallet.getAmount());
    }


    @Test
    @DisplayName("Вывод баланса счета")
    void getWalletBalanceSuccess() {
        UUID uuid = UUID.fromString("5c480ca5-2f96-4253-b59d-ad3bdc5d7218");
        Wallet wallet = new Wallet("login", Currency.BTC, BigDecimal.valueOf(10), uuid);
        walletsDb.getData().put(uuid, wallet);
        usersDb.getData().get("login").getWallets().add(wallet);
        BigDecimal result = RestAssured.given()
                .port(8008)
                .contentType(ContentType.JSON)
                .log().all()
                .get("/cryptowallet/balance/5c480ca5-2f96-4253-b59d-ad3bdc5d7218")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .as(BigDecimal.class);
        Assertions.assertEquals(BigDecimal.valueOf(1_000), result);
    }

    @Test
    @DisplayName("Вывод баланса всех счетов")
    void showAllWalletsBalanceSuccess() {
        UUID uuid = UUID.fromString("5c480ca5-2f96-4253-b59d-ad3bdc5d7218");
        Wallet wallet = new Wallet("login", Currency.BTC, BigDecimal.valueOf(10), uuid);
        walletsDb.getData().put(uuid, wallet);
        usersDb.getData().get("login").getWallets().add(wallet);
        UUID uuid2 = UUID.fromString("5c480ca5-2f96-4253-b59d-ad3bdc5d7219");
        Wallet wallet2 = new Wallet("login", Currency.SOL, BigDecimal.valueOf(10), uuid2);
        walletsDb.getData().put(uuid2, wallet2);
        usersDb.getData().get("login").getWallets().add(wallet2);
        BigDecimal result = RestAssured.given()
                .port(8008)
                .contentType(ContentType.JSON)
                .queryParam("username", "login")
                .log().all()
                .get("/cryptowallet/balance")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .as(BigDecimal.class);
        Assertions.assertEquals(BigDecimal.valueOf(2_000), result);
    }
}
