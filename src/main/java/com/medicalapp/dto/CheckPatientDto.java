// src/main/java/com/medicalapp/dto/CheckPatientDto.java
package com.medicalapp.dto;

public class CheckPatientDto {
    private String lastName;
    private String firstName;
    private String middleName;
    private String passportSeries;
    private String passportNumber;
    private String passportIssueDate;   // dd.MM.yyyy
    private String passportIssuedBy;
    private String identificationNumber;

    public CheckPatientDto() {}

    // --- getters / setters ---
    public String getLastName()             { return lastName; }
    public void setLastName(String ln)      { lastName = ln; }
    public String getFirstName()            { return firstName; }
    public void setFirstName(String fn)     { firstName = fn; }
    public String getMiddleName()           { return middleName; }
    public void setMiddleName(String mn)    { middleName = mn; }
    public String getPassportSeries()       { return passportSeries; }
    public void setPassportSeries(String s) { passportSeries = s; }
    public String getPassportNumber()       { return passportNumber; }
    public void setPassportNumber(String n) { passportNumber = n; }
    public String getPassportIssueDate()    { return passportIssueDate; }
    public void setPassportIssueDate(String d){ passportIssueDate = d; }
    public String getPassportIssuedBy()     { return passportIssuedBy; }
    public void setPassportIssuedBy(String i){ passportIssuedBy = i; }
    public String getIdentificationNumber(){ return identificationNumber; }
    public void setIdentificationNumber(String id){ identificationNumber = id; }
}
