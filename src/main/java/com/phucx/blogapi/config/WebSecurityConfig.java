package com.phucx.blogapi.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@ComponentScans({
    @ComponentScan("com.phucx.blogapi.authenticationProvider"),
    @ComponentScan("com.phucx.blogapi.filter")
})
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain dFilterChain(HttpSecurity http) throws Exception{

        http.sessionManagement(sesison -> sesison.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        http.csrf(csrf-> csrf.disable());
        http.cors(cors->cors.configurationSource(new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest arg0) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
                return configuration;
            }
            
        }));
        // login
        // http.formLogin(Customizer.withDefaults());
        http.formLogin(login -> login.loginPage("/login"));
        http.httpBasic(Customizer.withDefaults());
        http.logout(logout -> logout.invalidateHttpSession(true));
        // request
        http.authorizeHttpRequests(request -> request
            .requestMatchers("/posts/**", "/register/**").permitAll()
            .requestMatchers("/blogOwner/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated());
        return http.build();
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
