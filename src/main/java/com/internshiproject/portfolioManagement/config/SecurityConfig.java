package com.internshiproject.portfolioManagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())

                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())

                .authorizeHttpRequests(auth -> auth

                        // ================= PUBLIC AUTH APIs =================
                        .requestMatchers(
                                "/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // ================= PUBLIC FRONTEND =================
                        .requestMatchers(
                                "/",
                                "/login.html",
                                "/register.html",
                                "/dashboard.html",
                                "/admin.html",
                                "/favicon.ico",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/assets/**"
                        ).permitAll()

                        // ================= PUBLIC STOCK DATA =================
                        .requestMatchers(
                                "/api/stock/**"
                        ).permitAll()

                        // ================= CORS PREFLIGHT =================
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // ================= ADMIN ONLY =================
                        .requestMatchers(
                                "/admin/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/admin-message/send"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/admin-message/all"
                        ).hasRole("ADMIN")

                        // ================= LOGGED-IN USERS =================
                        .requestMatchers(
                                "/api/**"
                        ).authenticated()

                        // Everything else remains protected
                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
