// src/main/java/com/medicalapp/dto/DoctorProfileDto.java
package com.medicalapp.dto;

public class DoctorProfileDto {
    private String email;
    private String registered;
    private String type;

    public DoctorProfileDto(String email, String registered, String type) {
        this.email = email;
        this.registered = registered;
        this.type = type;
    }

    public String getEmail() { return email; }
    public String getRegistered() { return registered; }
    public String getType() { return type; }
}