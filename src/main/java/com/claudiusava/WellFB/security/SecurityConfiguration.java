package com.claudiusava.WellFB.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

    private static final String[] user_auth = {
            "/",
            "/post/**",
            "/uploads/**"
    };

    private static final String[] anon_auth = {
            "/css/styles.css",
            "/js/script.js",
            "/users/new",
            "/drawable/**",
            "avatars/default_avatar_100.png"
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

                .csrf(csrf -> csrf.disable())

                .headers(header -> header.frameOptions( h-> h.disable()));


        return http.build();
    }


    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
