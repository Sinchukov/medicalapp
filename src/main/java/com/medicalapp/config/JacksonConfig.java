//// src/main/java/com/medicalapp/config/JacksonConfig.java
//package com.medicalapp.config;
//
//import com.fasterxml.jackson.databind.Module;
//import com.fasterxml.jackson.databind.jsontype.NamedType;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
//import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.format.DateTimeFormatter;
//import java.time.LocalDate;
//
//@Configuration
//public class JacksonConfig {
//
//    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");
//
//    @Bean
//    public Module javaTimeModule() {
//        JavaTimeModule module = new JavaTimeModule();
//        // 1) сериализация в "dd.MM.yyyy"
//        module.addSerializer(LocalDate.class, new LocalDateSerializer(DMY));
//        // 2) ваш десериализатор (dd.MM.yyyy или ISO-8601)
//        module.addDeserializer(LocalDate.class, new LocalDateMultiFormatDeserializer());
//        return module;
//    }
//
//    // Установка модуля в ObjectMapper
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
//        return builder -> builder.modulesToInstall(javaTimeModule());
//    }
//}
