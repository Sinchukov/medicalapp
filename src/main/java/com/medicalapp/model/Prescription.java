package com.medicalapp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // название лекарства
    @Column(name = "medication_name", nullable = false)
    private String drugName;

    @Column(nullable = false)
    private String dosage;

    // дата выписки (дата без времени)
    @Column(name = "prescription_issue_date", nullable = false)
    private LocalDate issueDate;
    @Column(name = "prescription_issue_datetime", nullable = false)
    private LocalDateTime issueDateTime;

    @Column(name = "prescription_expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "doctor_email", nullable = false)
    private String doctorEmail;

    @Column(name = "patient_email", nullable = false)
    private String patientEmail;

    @Column(nullable = false)
    private boolean dispensed;

    // === Геттеры / сеттеры ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDrugName() { return drugName; }
    public void setDrugName(String drugName) { this.drugName = drugName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

   public LocalDateTime getIssueDateTime() { return issueDateTime; }
   public void setIssueDateTime(LocalDateTime issueDateTime) { this.issueDateTime = issueDateTime; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDoctorEmail() { return doctorEmail; }
    public void setDoctorEmail(String doctorEmail) { this.doctorEmail = doctorEmail; }

    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }

    public boolean isDispensed() { return dispensed; }
    public void setDispensed(boolean dispensed) { this.dispensed = dispensed; }
}
