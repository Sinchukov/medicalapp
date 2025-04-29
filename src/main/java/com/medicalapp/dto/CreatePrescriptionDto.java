
package com.medicalapp.dto;

import java.time.LocalDate;

public class CreatePrescriptionDto {
    private String patientEmail;
    private String drugName;
    private String dosage;
    private LocalDate expiry;

    // getters/setters
    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }
    public String getDrugName() { return drugName; }
    public void setDrugName(String drugName) { this.drugName = drugName; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public LocalDate getExpiry() { return expiry; }
    public void setExpiry(LocalDate expiry) { this.expiry = expiry; }
}
