// src/main/java/com/medicalapp/controller/LoginDto.java
package com.medicalapp.controller;

public class LoginDto {
    private String email;
    private String password;
    public LoginDto() {}
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}