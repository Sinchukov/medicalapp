// src/main/java/com/medicalapp/dto/PatientPersonalInfoDto.java
package com.medicalapp.dto;

/** Все поля — String, дата в формате dd.MM.yyyy */
public class PatientPersonalInfoDto {
    private String lastName;
    private String firstName;
    private String middleName;
    private String passportSeries;
    private String passportNumber;
    private String passportIssueDate;
    private String passportIssuedBy;
    private String identificationNumber;

    public PatientPersonalInfoDto() {}

    public PatientPersonalInfoDto(String lastName,
                                  String firstName,
                                  String middleName,
                                  String passportSeries,
                                  String passportNumber,
                                  String passportIssueDate,
                                  String passportIssuedBy,
                                  String identificationNumber) {
        this.lastName            = lastName;
        this.firstName           = firstName;
        this.middleName          = middleName;
        this.passportSeries      = passportSeries;
        this.passportNumber      = passportNumber;
        this.passportIssueDate   = passportIssueDate;
        this.passportIssuedBy    = passportIssuedBy;
        this.identificationNumber= identificationNumber;
    }
    // --- геттеры и сеттеры ---
    public String getLastName()  { return lastName; }
    public void   setLastName(String lastName) { this.lastName = lastName; }
    public String getFirstName() { return firstName; }
    public void   setFirstName(String firstName) { this.firstName = firstName; }
    public String getMiddleName(){ return middleName; }
    public void   setMiddleName(String middleName) { this.middleName = middleName; }
    public String getPassportSeries() { return passportSeries; }
    public void   setPassportSeries(String passportSeries) { this.passportSeries = passportSeries; }
    public String getPassportNumber() { return passportNumber; }
    public void   setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
    public String getPassportIssueDate() { return passportIssueDate; }
    public void   setPassportIssueDate(String passportIssueDate) { this.passportIssueDate = passportIssueDate; }
    public String getPassportIssuedBy() { return passportIssuedBy; }
    public void   setPassportIssuedBy(String passportIssuedBy) { this.passportIssuedBy = passportIssuedBy; }
    public String getIdentificationNumber() { return identificationNumber; }
    public void   setIdentificationNumber(String identificationNumber) { this.identificationNumber = identificationNumber; }
}
