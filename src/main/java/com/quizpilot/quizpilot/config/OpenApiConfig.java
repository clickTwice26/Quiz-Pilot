package com.quizpilot.quizpilot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("QuizPilot API")
                        .description("Interactive Quiz Application API - AI powered quiz preparation platform\n\n" +
                                    "**Authentication Instructions:**\n" +
                                    "1. First, register a new user using `/api/users/register`\n" +
                                    "2. Login using `/api/users/login` to get a session token\n" +
                                    "3. Click the 'Authorize' button below and enter: `Bearer YOUR_SESSION_TOKEN`\n" +
                                    "4. Now you can access all secured endpoints!")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("QuizPilot Team")
                                .email("support@quizpilot.com")
                                .url("https://github.com/clickTwice26/Quiz-Pilot"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Server"),
                        new Server()
                                .url("https://api.quizpilot.com")
                                .description("Production Server")
                ))
                // Add security scheme
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter your session token here (without 'Bearer ' prefix)")
                        )
                );
    }
}
