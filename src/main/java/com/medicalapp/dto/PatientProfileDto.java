// src/main/java/com/medicalapp/dto/PatientProfileDto.java
package com.medicalapp.dto;

public class PatientProfileDto {
    private String email;
    private String registered;
    private String type;

    public PatientProfileDto(String email, String registered, String type) {
        this.email = email;
        this.registered = registered;
        this.type = type;
    }

    public String getEmail() { return email; }
    public String getRegistered() { return registered; }
    public String getType() { return type; }
}