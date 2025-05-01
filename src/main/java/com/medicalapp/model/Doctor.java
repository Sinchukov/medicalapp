package com.medicalapp.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // базовые поля
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDate registrationDate;

    // Personal Info
    private String lastName;
    private String firstName;
    private String middleName;

    @Column(name = "passport_series_and_number", nullable = false)
    private String passportSeriesAndNumber;
    private LocalDate passportIssueDate;
    private String passportIssuedBy;
    private String identificationNumber;

    // Doctor-specific
    private String qualification;
    private String experience;
    private String workplace;

    // --- Constructors, getters, setters ---

    public Doctor() {
        this.registrationDate = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getPassportSeriesAndNumber() {
        return passportSeriesAndNumber;
    }
    public void setPassportSeriesAndNumber(String passportSeriesAndNumber) {
        this.passportSeriesAndNumber = passportSeriesAndNumber;
    }

    public LocalDate getPassportIssueDate() { return passportIssueDate; }
    public void setPassportIssueDate(LocalDate passportIssueDate) { this.passportIssueDate = passportIssueDate; }

    public String getPassportIssuedBy() { return passportIssuedBy; }
    public void setPassportIssuedBy(String passportIssuedBy) { this.passportIssuedBy = passportIssuedBy; }

    public String getIdentificationNumber() { return identificationNumber; }
    public void setIdentificationNumber(String identificationNumber) { this.identificationNumber = identificationNumber; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getWorkplace() { return workplace; }
    public void setWorkplace(String workplace) { this.workplace = workplace; }
}