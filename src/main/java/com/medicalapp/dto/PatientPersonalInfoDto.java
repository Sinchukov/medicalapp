// src/main/java/com/medicalapp/dto/PatientPersonalInfoDto.java
package com.medicalapp.dto;

import java.time.LocalDate;

public class PatientPersonalInfoDto {
    private String lastName;
    private String firstName;
    private String middleName;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssuedBy;
    private String identificationNumber;

    public PatientPersonalInfoDto(
            String lastName,
            String firstName,
            String middleName,
            String passportSeries,
            String passportNumber,
            LocalDate passportIssueDate,
            String passportIssuedBy,
            String identificationNumber
    ) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
        this.passportIssueDate = passportIssueDate;
        this.passportIssuedBy = passportIssuedBy;
        this.identificationNumber = identificationNumber;
    }

    // геттеры...
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getPassportSeries() { return passportSeries; }
    public String getPassportNumber() { return passportNumber; }
    public LocalDate getPassportIssueDate() { return passportIssueDate; }
    public String getPassportIssuedBy() { return passportIssuedBy; }
    public String getIdentificationNumber() { return identificationNumber; }
}
