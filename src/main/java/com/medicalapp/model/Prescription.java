package com.medicalapp.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "patient_email")
    private String patientEmail;

    @Column(nullable = false, name = "doctor_email")
    private String doctorEmail;

    /**
     * В БД этот столбец называется medication_name,
     * поэтому указываем name="medication_name"
     */
    @Column(nullable = false, name = "medication_name")
    private String drugName;

    @Column(nullable = false)
    private String dosage;

    @Column(nullable = false, name = "prescription_issue_date")
    private LocalDate issueDate;

    @Column(nullable = false, name = "prescription_expiry_date")
    private LocalDate expiryDate;

    @Column(nullable = false)
    private String status;

    // === геттеры и сеттеры ===

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

    public LocalDate getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
