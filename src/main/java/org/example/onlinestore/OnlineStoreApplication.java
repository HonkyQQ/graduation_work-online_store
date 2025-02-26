package org.example.onlinestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example.onlinestore")
public class OnlineStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineStoreApplication.class, args);
    }
}