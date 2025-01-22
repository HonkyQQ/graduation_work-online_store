package org.example.onlinestore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner {

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            System.out.println("Приложение запущено и работает...");
            Thread.sleep(60000);
        };
    }
}