package com.corporate.travel.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI corporateTravelOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Corporate Business Travel Management API")
                        .description("Production-ready enterprise SaaS REST APIs for managing business travel, corporate policies, multi-tier approvals, booking, expense tracking, AI trip recommendations, risk center, and executive analytics.")
                        .version("v1.0.0")
                        .contact(new Contact().name("Enterprise Travel Platform Team").email("support@corporatetravel.com"))
                        .license(new License().name("Enterprise Commercial License")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }


    public OpenAPIConfig() {}

    public static OpenAPIConfigBuilder builder() { return new OpenAPIConfigBuilder(); }

    public static class OpenAPIConfigBuilder {


        public OpenAPIConfig build() {
            OpenAPIConfig obj = new OpenAPIConfig();
            return obj;
        }
    }
}
