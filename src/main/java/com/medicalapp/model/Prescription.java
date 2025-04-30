package com.medicalapp.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_email", nullable = false)
    private String patientEmail;

    @Column(name = "doctor_email", nullable = false)
    private String doctorEmail;

    @Column(name = "medication_name", nullable = false)
    private String drugName;

    @Column(nullable = false)
    private String dosage;

    // ------------------------------
    // СОВПАДАЕТ С prescription_issue_date
    @Column(name = "prescription_issue_date", nullable = false)
    private LocalDate issueDate;

    // СОВПАДАЕТ С prescription_expiry_date
    @Column(name = "prescription_expiry_date", nullable = false)
    private LocalDate expiryDate;

    // геттеры/сеттеры

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }

    public String getDoctorEmail() { return doctorEmail; }
    public void setDoctorEmail(String doctorEmail) { this.doctorEmail = doctorEmail; }

    public String getDrugName() { return drugName; }
    public void setDrugName(String drugName) { this.drugName = drugName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public LocalDate getDateIssued() { return issueDate; }
    public void setDateIssued(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}
