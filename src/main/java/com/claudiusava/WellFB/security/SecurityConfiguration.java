package com.claudiusava.WellFB.security;


import com.claudiusava.WellFB.service.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

    private static final String[] user_auth = {
            "/",
            "/post/**",
            "/user/**",
            "/resources/uploads/**"
    };

    private static final String[] anon_auth = {
            "/users/new"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                        .requestMatchers(user_auth).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(anon_auth).permitAll()
                .anyRequest().authenticated())

                .formLogin(login -> login
                        .permitAll()
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true))

                .logout(logout -> logout.permitAll())

                .csrf(csrf -> csrf.disable());

        return http.build();
    }


    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
