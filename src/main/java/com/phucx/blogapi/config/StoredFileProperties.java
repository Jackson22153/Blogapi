package com.phucx.blogapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter @Setter
@ConfigurationProperties(prefix = "file")
public class StoredFileProperties {
    private String imageStoredDir;
}
