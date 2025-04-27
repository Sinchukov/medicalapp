// src/main/java/com/medicalapp/model/Prescription.java
package com.medicalapp.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Email пациента, для фильтрации
    @Column(nullable = false)
    private String patientEmail;

    // Email врача, для фильтрации
    @Column(nullable = false)
    private String doctorEmail;

    @Column(nullable = false)
    private String drugName;

    @Column(nullable = false)
    private String dosage;

    @Column(nullable = false)
    private LocalDate expiry;

    @Column(nullable = false)
    private LocalDate dateIssued;

    // --- геттеры / сеттеры ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientEmail() {
        return patientEmail;
    }
    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }
    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDrugName() {
        return drugName;
    }
    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDosage() {
        return dosage;
    }
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public LocalDate getExpiry() {
        return expiry;
    }
    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    public LocalDate getDateIssued() {
        return dateIssued;
    }
    public void setDateIssued(LocalDate dateIssued) {
        this.dateIssued = dateIssued;
    }
}
