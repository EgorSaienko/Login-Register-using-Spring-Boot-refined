package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфігурація MVC, яка додає прості ViewController-и без необхідності створення контролерів.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Додає ViewController-и для доступу до статичних сторінок без логіки контролера.
     *
     * @param registry Реєстр контролерів представлення
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/access-denied").setViewName("access-denied");
        registry.addViewController("/").setViewName("homepage");
        registry.addViewController("/about-us").setViewName("about-us");
    }
}
