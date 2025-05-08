package com.medicalapp.controller;

public class PharmacyRecipeDto {
    private Long prescriptionId;
    private String dateIssued;
    private String expiryDate;
    private String medicine;
    private String dosage;
    private String doctorEmail;
    private String status;     // статус ISSUED или DISPENSED

    // геттеры/сеттеры для всех полей
    public Long getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(Long prescriptionId) { this.prescriptionId = prescriptionId; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
