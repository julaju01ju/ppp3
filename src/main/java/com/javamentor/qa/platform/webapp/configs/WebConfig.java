package com.javamentor.qa.platform.webapp.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**/*")
                .addResourceLocations("/", "/resources/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("/html/login");
        registry.addViewController("/question/add").setViewName("/html/question/add.html");
        WebMvcConfigurer.super.addViewControllers(registry);
    }
}
//Привет, добавлять пользователей
// (правильнее будет сказать всех данных, не только же User там будут )
// будет это класс, а вызываться метод инициализации будет в
// config, webapp/config/init...
