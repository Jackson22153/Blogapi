package com.phucx.blogapi.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {
    @Value("${phucxopenai.dev-url}")
    private String devUrl;

    @Bean
    public OpenAPI myOpenAPI(){
        Server serverDevUrl = new Server();
        serverDevUrl.setUrl(devUrl);
        serverDevUrl.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("trongphuc22153@gmail.com");
        contact.setName("phucx");

        Info info = new Info()
            .description("This API exposes endpoints to manage tutorials.")
            .contact(contact)
            .version("1.0.0")
            .title("Tutorial Management API");
        return new OpenAPI().info(info).servers(List.of(serverDevUrl));
    }
}
