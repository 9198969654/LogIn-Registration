package com.loginUser.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class UserRegistrationSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors() // Enable Cross-Origin Resource Sharing (CORS)
                .and()
                .csrf()
                .disable() // Disable CSRF protection
                .authorizeHttpRequests() // Authorize HTTP requests
                .requestMatchers("/register/**") // Specify requests to /register
                .permitAll() // Allow unauthenticated access to /register
                .and()
                .authorizeHttpRequests() // Authorize HTTP requests again
                .requestMatchers("/users/**") // Specify requests to /users
                .hasAnyAuthority("USER", "ADMIN") // Allow access to /users for users with "USER" or "ADMIN" authority
                .and()
                .formLogin() // Enable form-based login
                .and()
                .build(); // Build and return the security filter chain
    }



}
