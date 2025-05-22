package com.awbd.awbd.config;

import com.awbd.awbd.service.JpaUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@Profile("postgres")
public class SecurityJpaConfig {

    private final JpaUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/webjars/**", "/login", "/register", "/resources/**").permitAll()
                        .requestMatchers("/vehicle/**").hasRole("CLIENT")
                        .requestMatchers("/mechanic/**").hasRole("CLIENT")
                        .requestMatchers("/appointment/form").hasRole("CLIENT")
                        .requestMatchers("/appointment").hasAnyRole("CLIENT", "MECHANIC")
                        .requestMatchers("/service-type/**").hasRole("MECHANIC")
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .permitAll()
                                .loginProcessingUrl("/perform_login")
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/access_denied"))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}