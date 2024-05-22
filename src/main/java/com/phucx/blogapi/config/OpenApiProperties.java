package com.phucx.blogapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.ToString;

@Configuration
@Data @ToString
@ConfigurationProperties(prefix = "phucxopenai")
public class OpenApiProperties {
    private String devUrl;
}
