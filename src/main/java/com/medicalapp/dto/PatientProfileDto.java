// src/main/java/com/medicalapp/dto/PatientProfileDto.java
package com.medicalapp.dto;

public class PatientProfileDto {
    private String type;
    private String email;
    private String registered;

    public PatientProfileDto() {}

    public PatientProfileDto(String type, String email, String registered) {
        this.type       = type;
        this.email      = email;
        this.registered = registered;
    }

    // геттеры/сеттеры
    public String getType()        { return type; }
    public void   setType(String type) { this.type = type; }
    public String getEmail()       { return email; }
    public void   setEmail(String email) { this.email = email; }
    public String getRegistered()  { return registered; }
    public void   setRegistered(String registered) { this.registered = registered; }
}