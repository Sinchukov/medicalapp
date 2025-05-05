// src/main/java/com/medicalapp/dto/PharmacyRecipeDto.java
package com.medicalapp.dto;

public class PharmacyRecipeDto {
    private Long prescriptionId;
    private String drugName;
    private String dosage;
    private String dateIssued;
    private String expiryDate;
    private String status;
    private String email;

    // геттеры/сеттеры
    // ...
    public Long getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(Long prescriptionId) { this.prescriptionId = prescriptionId; }
    public String getDrugName() { return drugName; }
    public void setDrugName(String drugName) { this.drugName = drugName; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public String getDateIssued() { return dateIssued; }
    public void setDateIssued(String dateIssued) { this.dateIssued = dateIssued; }
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void setDoctorEmail(String doctorEmail) {
        this.email=email;
    }
}
