package com.medicalapp.model;

public abstract class UserBase {
    public abstract Long getId();
    public abstract String getEmail();
    public abstract String getPassword();
    public abstract Role   getRole();
}
