// src/main/java/com/medicalapp/dto/DoctorPersonalInfoDto.java
package com.medicalapp.dto;

import java.time.LocalDate;

public class DoctorPersonalInfoDto {
    private String lastName;
    private String firstName;
    private String middleName;
    private String passportSeriesAndNumber;
    private LocalDate passportIssueDate;
    private String passportIssuedBy;
    private String identificationNumber;
    private String qualification;
    private String experience;
    private String workplace;

    public DoctorPersonalInfoDto(
            String lastName,
            String firstName,
            String middleName,
            String passportSeriesAndNumber,
            LocalDate passportIssueDate,
            String passportIssuedBy,
            String identificationNumber,
            String qualification,
            String experience,
            String workplace
    ) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.passportSeriesAndNumber = passportSeriesAndNumber;
        this.passportIssueDate = passportIssueDate;
        this.passportIssuedBy = passportIssuedBy;
        this.identificationNumber = identificationNumber;
        this.qualification = qualification;
        this.experience = experience;
        this.workplace = workplace;
    }

    // геттеры...
    public String getPassportSeriesAndNumber() {
        return passportSeriesAndNumber;
    }
    public void setPassportSeriesAndNumber(String passportSeriesAndNumber) {
        this.passportSeriesAndNumber = passportSeriesAndNumber;
    }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public LocalDate getPassportIssueDate() { return passportIssueDate; }
    public String getPassportIssuedBy() { return passportIssuedBy; }
    public String getIdentificationNumber() { return identificationNumber; }
    public String getQualification() { return qualification; }
    public String getExperience() { return experience; }
    public String getWorkplace() { return workplace; }
}