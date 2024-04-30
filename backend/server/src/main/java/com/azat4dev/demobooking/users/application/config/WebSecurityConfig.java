package com.azat4dev.demobooking.users.application.config;

import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.interfaces.services.EmailService;
import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.domain.interfaces.services.PasswordService;
import com.azat4dev.demobooking.users.domain.services.EmailData;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.Password;
import com.azat4dev.demobooking.users.presentation.security.services.CustomUserDetailsService;
import com.azat4dev.demobooking.users.presentation.security.services.CustomUserDetailsServiceImpl;
import com.azat4dev.demobooking.users.presentation.security.services.JwtAuthenticationFilter;
import com.azat4dev.demobooking.users.presentation.security.services.JwtAuthenticationProvider;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.JWTService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
    SecurityFilterChain filterChain(
        HttpSecurity http,
        JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {
        return http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(
                requests -> requests.requestMatchers(
                        HttpMethod.POST,
                        "/api/public/**"
                    )
                    .permitAll()
                    .requestMatchers("/api/with-auth/**")
                    .authenticated()
            )
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
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new JwtAuthenticationFilter(authenticationManager);
    }

    @Bean
    public AuthenticationManager authenticationManager(
        CustomUserDetailsService customUserDetailsService,
        PasswordEncoder passwordEncoder,
        JWTService jwtService
    ) {

        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(
            jwtService,
            customUserDetailsService
        );

        DaoAuthenticationProvider userNameAuthenticationProvider = new DaoAuthenticationProvider();
        userNameAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        userNameAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(
            jwtAuthenticationProvider,
            userNameAuthenticationProvider
        );
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService(UsersRepository usersRepository) {
        return new CustomUserDetailsServiceImpl(
            usersRepository
        );
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new RequestAttributeSecurityContextRepository();
    }
}
