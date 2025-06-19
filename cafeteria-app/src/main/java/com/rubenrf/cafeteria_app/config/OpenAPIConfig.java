package com.rubenrf.cafeteria_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

        @Bean
        public OpenAPI cafeteriaServiceAPI() {
                return new OpenAPI()
                                .info(new Info().title("Cafeteria API")
                                                .description("Este es la API REST para el servicio de Cafeteria")
                                                .version("v.1.0.0").license(new License().name("Apache 2.0")));
        }

}
