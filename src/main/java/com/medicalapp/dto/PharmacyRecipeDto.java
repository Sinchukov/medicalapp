// src/main/java/com/medicalapp/dto/PharmacyRecipeDto.java
package com.medicalapp.dto;

public class PharmacyRecipeDto {
    private String dateIssued;   // dd.MM.yyyy
    private String expiryDate;   // dd.MM.yyyy
    private String medicine;
    private String dosage;
    private String doctorEmail;

    // геттеры / сеттеры
    public String getDateIssued() { return dateIssued; }
    public void setDateIssued(String dateIssued) { this.dateIssued = dateIssued; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getMedicine() { return medicine; }
    public void setMedicine(String medicine) { this.medicine = medicine; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getDoctorEmail() { return doctorEmail; }
    public void setDoctorEmail(String doctorEmail) { this.doctorEmail = doctorEmail; }
}
