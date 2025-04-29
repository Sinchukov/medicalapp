//// src/main/java/com/medicalapp/config/LocalDateDMYDeserializer.java
//package com.medicalapp.config;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.exc.InvalidFormatException;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//
///**
// * Принимает ТОЛЬКО формат dd.MM.yyyy.
// * В противном случае бросает Bad Request.
// */
//public class LocalDateDMYDeserializer extends JsonDeserializer<LocalDate> {
//    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");
//    @Override
//    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//        String str = p.getText().trim();
//        try {
//            return LocalDate.parse(str, DMY);
//        } catch (DateTimeParseException ex) {
//            throw InvalidFormatException.from(
//                    p,
//                    String.format("Expected date format dd.MM.yyyy but got '%s'", str),
//                    str,
//                    LocalDate.class
//            );
//        }
//    }
//}
