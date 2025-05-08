package com.medicalapp.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Поскольку в БД есть оба столбца drug_name и medication_name,
    // но фактически обязательным (NOT NULL) стоит medication_name,
    // мы будем маппить:
    @Column(name = "medication_name", nullable = false)
    private String drugName;

    @Column(nullable = false)
    private String dosage;

    @Column(name = "prescription_issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "prescription_expiry_date", nullable = false)
    private LocalDate expiryDate;

    // В БД нет столбца `dispensed`, но есть `status` — видимо, именно он
    // хранит состояние рецепта (ISSUED / DISPENSED)
    // Поэтому:
    @Column(name = "status", nullable = false)
    private String status;

    // Удаляем булево поле dispensed — вместо него оперируем строковым status.
    // Там, где нужно булево, проверяем status.equals("DISPENSED") и т.п.

    // Поля doctorEmail и patientEmail маппим по умолчанию:
    @Column(name = "doctor_email", nullable = false)
    private String doctorEmail;

    @Column(name = "patient_email", nullable = false)
    private String patientEmail;

    // === Геттеры/сеттеры ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDrugName() { return drugName; }
    public void setDrugName(String drugName) { this.drugName = drugName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDoctorEmail() { return doctorEmail; }
    public void setDoctorEmail(String doctorEmail) { this.doctorEmail = doctorEmail; }

    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }
    @Column(nullable = false)
    private boolean dispensed;  // false — ещё не выдано, true — выдано

    public boolean isDispensed() { return dispensed; }
    public void setDispensed(boolean dispensed) { this.dispensed = dispensed; }

}
