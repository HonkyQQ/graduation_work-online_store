package org.example.onlinestore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF можно включить позже, если потребуется
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/catalog/**",
                                "/product/**",
                                "/auth/register",
                                "/auth/login",
                                "/images/**",
                                "/static/**",
                                "/cart/**",
                                "/orders/**",
                                "/css/**"
                        ).permitAll() // Разрешаем доступ к указанным путям без авторизации
                        .anyRequest().authenticated() // Остальные запросы требуют авторизации
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // Указываем страницу логина
                        .usernameParameter("email") // Параметр для email
                        .passwordParameter("password") // Параметр для пароля
                        .defaultSuccessUrl("/", true) // Перенаправление на главную после успешного входа
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для выхода
                        .logoutSuccessUrl("/") // Перенаправление после выхода
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}