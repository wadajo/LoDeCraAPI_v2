package com.lodecra.apiV1.config;

import com.lodecra.apiV1.config.auth.CustomAccessDeniedHandler;
import com.lodecra.apiV1.config.auth.CustomAuthenticationFailureHandler;
import com.lodecra.apiV1.util.Utilidades;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final Utilidades util;

    public SecurityConfig(Utilidades util) {
        this.util = util;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
                .username("julio")
                .password("{bcrypt}$2a$10$qprkYOE0NzmaRrSyicvQl.X0sOt/B4c1O6DiK/SDEaKArm1JhtY2y")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("antonio")
                .password("{bcrypt}$2a$10$S3mIQDmGLj.4B0LFVQxx9.xZfvMsx0dKrrTs0O9lok8DcwoDmDXkW")
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user,admin);
    }

    @Bean
    public SecurityFilterChain customFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests()
                .anyRequest()
                .hasAnyRole("USER","ADMIN")
                .and()
                .httpBasic(withDefaults())
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler())
                    .authenticationEntryPoint(authenticationFailureHandler())
                .and()
                .build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(util);
    }

}