package com.azat4dev.booking.listingsms.config.common.infrastructure.api.rest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(
        HttpSecurity http,
        @Qualifier("accessTokenDecoder") JwtDecoder jwtDecoder
    ) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(c -> c.requestMatchers(
                        HttpMethod.POST,
                        "/api/public/**"
                    )
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.GET,
                        "/api/public/**"
                    ).permitAll()

                    .requestMatchers("/api/private/**")
                    .authenticated()
            )
            .csrf(c -> c.ignoringRequestMatchers("/api/public/**", "/api/private/**"))
            .httpBasic(Customizer.withDefaults())
            .oauth2ResourceServer(c -> c.jwt(jwtCustom -> jwtCustom.decoder(jwtDecoder)))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
            "/**",
            configuration
        );
        return source;
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new RequestAttributeSecurityContextRepository();
    }
}
