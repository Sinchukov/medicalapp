//// src/main/java/com/medicalapp/config/MvcConfig.java
//package com.medicalapp.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.*;
//
//@Configuration
//public class MvcConfig implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // Отдаём только файлы *.html, *.js, *.css и favicon.ico из /static
//        registry.addResourceHandler("/*.html", "/**/*.html", "/js/**", "/css/**", "/favicon.ico")
//                .addResourceLocations("classpath:/static/");
//    }
//}
