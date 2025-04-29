//// src/main/java/com/medicalapp/config/LocalDateMultiFormatDeserializer.java
//package com.medicalapp.config;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.exc.InvalidFormatException;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//
//public class LocalDateMultiFormatDeserializer extends JsonDeserializer<LocalDate> {
//    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");
//
//    @Override
//    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//        String text = p.getText().trim();
//        // сначала пробуем dd.MM.yyyy
//        try {
//            return LocalDate.parse(text, DMY);
//        } catch (DateTimeParseException ignore) {
//            // fallback к ISO yyyy-MM-dd
//            try {
//                return LocalDate.parse(text);
//            } catch (DateTimeParseException ex2) {
//                throw InvalidFormatException.from(p,
//                        "Cannot parse date '" + text + "'", text, LocalDate.class);
//            }
//        }
//    }
//}
