package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Головний клас Spring Boot-застосунку.
 * Запускає застосунок через метод main().
 */
@SpringBootApplication
public class LoginRegisterApplication {

    /**
     * Точка входу у Spring Boot-застосунок.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        SpringApplication.run(LoginRegisterApplication.class, args);
    }

}
