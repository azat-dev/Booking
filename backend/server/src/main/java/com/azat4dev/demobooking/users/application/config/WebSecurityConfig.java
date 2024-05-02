package com.azat4dev.demobooking.users.application.config;

import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.interfaces.services.EmailService;
import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.domain.interfaces.services.PasswordService;
import com.azat4dev.demobooking.users.domain.services.EmailData;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.Password;
import com.azat4dev.demobooking.users.presentation.security.services.CustomUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(c -> c.requestMatchers(
                    HttpMethod.POST,
                    "/api/public/**"
                )
                .permitAll()
                .requestMatchers("/api/with-auth/**")
                .authenticated())
            .csrf(c -> c.ignoringRequestMatchers("/api/public/**", "/api/with-auth/**"))
            .httpBasic(Customizer.withDefaults())
            .oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()))
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
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    PasswordService passwordService(PasswordEncoder passwordEncoder) {
        return new PasswordService() {
            @Override
            public EncodedPassword encodePassword(Password password) {
                return new EncodedPassword(passwordEncoder.encode(password.getValue()));
            }
        };
    }

    @Bean
    EmailService emailService() {
        return new EmailService() {
            @Override
            public void send(EmailAddress email, EmailData data) {
                System.out.println("Email sent to " + email);
            }
        };
    }

    @Bean
    public UserDetailsService customUserDetailsService(UsersRepository usersRepository) {
        return new CustomUserDetailsServiceImpl(
            usersRepository
        );
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new RequestAttributeSecurityContextRepository();
    }
}
