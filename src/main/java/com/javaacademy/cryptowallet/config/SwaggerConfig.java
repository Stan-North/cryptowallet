package com.javaacademy.cryptowallet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        Info info = new Info()
                .title("Api для криптокошелька")
                .description("Публичное апи криптокошелька");
        return new OpenAPI().info(info);
    }
}
