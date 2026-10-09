package com.medsupply.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Medicine Supply Chain Management API")
                        .version("1.0.0")
                        .description("RESTful API for tracking pharmaceutical inventories, consumption records, stock-out forecasts, and intelligent rebalancing shipments across regional healthcare facilities.")
                        .contact(new Contact()
                                .name("MedSupply Engineering")
                                .email("support@medsupply.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
}