// src/main/java/com/medicalapp/config/WebConfig.java
package com.medicalapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Используем только явные пути: html, css, js в /static
        registry.addResourceHandler("/*.html", "/css/**", "/js/**")
                .addResourceLocations("classpath:/static/");
    }
}
