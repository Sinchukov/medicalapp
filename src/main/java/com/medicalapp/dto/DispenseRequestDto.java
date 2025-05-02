// src/main/java/com/medicalapp/dto/DispenseRequestDto.java
package com.medicalapp.dto;

/**
 * DTO для запроса выдачи лекарства пациенту:
 * drugName и dosage — чтобы найти и «списать» рецепт,
 * quantity — количество единиц к выдаче (если нужно).
 */
public class DispenseRequestDto {
    private String patientEmail;
    private String drugName;
    private String dosage;

    public DispenseRequestDto() {}

    // Геттер и сеттер для patientEmail
    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
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
}