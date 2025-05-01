// src/main/java/com/medicalapp/dto/DoctorPrescriptionDto.java
package com.medicalapp.dto;

public class DoctorPrescriptionDto {
    // Вложенный DTO пациента
    public static class PatientInfo {
        public String email;
        public String firstName;
        public String lastName;
        public String middleName;
        public String passportSeriesAndNumber;
    }

    // Вложенный DTO рецепта
    public static class PrescriptionInfo {
        public String dateIssued;  // формат dd.MM.yyyy
        public String expiryDate;  // формат dd.MM.yyyy
        public String medicine;
        public String dosage;
    }

    public PatientInfo    patient;
    public PrescriptionInfo prescription;
}
