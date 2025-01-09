package com.javaacademy.cryptowallet;

import com.javaacademy.cryptowallet.data_base.UsersDb;
import com.javaacademy.cryptowallet.dto.ResetPassRequestDto;
import com.javaacademy.cryptowallet.dto.SaveUserDto;
import com.javaacademy.cryptowallet.entity.User;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("local")
class UserControllerTest {
    @Autowired
    private UsersDb usersDb;
    
    @Test
    @DisplayName("Регистрация пользователя")
    void SignUpSuccess() {
        SaveUserDto dto = new SaveUserDto("test", "example@gmail.com", "12345");
        RestAssured.given()
                .port(8008)
                .contentType(ContentType.JSON)
                .body(dto)
                .log().all()
                .post("/user/signup")
                .then()
                .log().all()
                .statusCode(201);
        Assertions.assertEquals(2, usersDb.getData().size());
        User user = usersDb.getData().get("test");
        Assertions.assertEquals("test", user.getLogin());
        Assertions.assertEquals("example@gmail.com", user.getEmail());
        Assertions.assertEquals("12345", user.getPassword());
    }

    @Test
    @DisplayName("Смена пароля")
    void resetPassSuccess() {
        ResetPassRequestDto dto = new ResetPassRequestDto("login", "12345", "1111");
        RestAssured.given()
                .port(8008)
                .contentType(ContentType.JSON)
                .body(dto)
                .log().all()
                .post("/user/reset-password")
                .then()
                .log().all()
                .statusCode(200);
        User user = usersDb.getData().get("login");
        Assertions.assertEquals("login", user.getLogin());
        Assertions.assertEquals("example@gmail.com", user.getEmail());
        Assertions.assertEquals("1111", user.getPassword());
    }
}
