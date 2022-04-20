package com.marketkurly.util;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenApiConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        Info info = new Info()
                .title("마켓컬리")
                .version("V1.0")
                .description("클론코딩 18조 마켓컬리")
                .contact(new Contact()
                        .name("Web Site")
                        .url("http://kbumsoo.s3-website.ap-northeast-2.amazonaws.com/"))
                .license(new License()
                        .name("Apache License Version 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0"));

        SecurityScheme auth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).in(SecurityScheme.In.HEADER).scheme("bearer").bearerFormat("");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("authorization");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("authorization", auth))
                .addSecurityItem(securityRequirement)
                .info(info);
    }
}