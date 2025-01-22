package org.example.onlinestore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключаем CSRF (если необходимо)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/", "/catalog/**").permitAll() // Разрешён доступ без авторизации
                        .anyRequest().authenticated() // Остальное требует авторизации
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // Ваша страница логина
                        .defaultSuccessUrl("/") // Перенаправление после успешного входа
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.build();
    }
}