package com.medicalapp.controller;

import com.medicalapp.model.Role;

public class RegisterDto {
    private Role role;
    private String email;
    private String password;
    private String name; // fullName or companyName

    public RegisterDto() {}
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}