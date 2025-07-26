package com.demok8s.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DemoK8s API")
                        .version("1.0.0")
                        .description("API for managing Kubernetes labs and pods")
                        .contact(new Contact()
                                .name("DemoK8s Team")
                                .email("admin@demok8s.com")));
    }
}
