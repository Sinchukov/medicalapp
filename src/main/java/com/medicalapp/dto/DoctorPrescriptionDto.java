package com.medicalapp.dto;

public class DoctorPrescriptionDto {
    public static class PatientInfo {
        public String email;
        public String firstName;
        public String lastName;
        public String middleName;
        public String passportSeriesAndNumber;
    }

    public static class PrescriptionInfo {
        public String dateIssued;
        public String expiryDate;
        public String medicine;
        public String dosage;
        public String status;   // <- добавили
    }

    public PatientInfo patient;
    public PrescriptionInfo prescription;
}
