// src/main/java/com/medicalapp/dto/RegisterDto.java
package com.medicalapp.dto;

public class RegisterDto {
    private String email;
    private String password;
    private String confirmPassword;
    private String role;         // PATIENT / DOCTOR / PHARMACY
    private String companyName;  // only for PHARMACY

    public RegisterDto() {}
    // --- getters / setters ---
    public String getEmail()             { return email; }
    public void setEmail(String e)       { email = e; }
    public String getPassword()          { return password; }
    public void setPassword(String p)    { password = p; }
    public String getConfirmPassword()   { return confirmPassword; }
    public void setConfirmPassword(String cp){ confirmPassword = cp; }
    public String getRole()              { return role; }
    public void setRole(String r)        { role = r; }
    public String getCompanyName()       { return companyName; }
    public void setCompanyName(String c) { companyName = c; }
}
