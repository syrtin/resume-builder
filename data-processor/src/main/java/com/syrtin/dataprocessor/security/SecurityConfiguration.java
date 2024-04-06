package com.syrtin.dataprocessor.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(HttpMethod.GET, "/resumes/new", "/api/resumes/**",
                                        "/api/uploadPhoto",  "/api/photos/**")
                                .hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/templates/**") // Специфичный путь для ADMIN
                                .hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe.key("AbraCadabra").tokenValiditySeconds(60 * 30))
                .logout(logout -> logout.logoutUrl("/signout").permitAll());
        return http.build();
    }

    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
