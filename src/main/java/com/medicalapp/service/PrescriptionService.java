package com.medicalapp.service;

import com.medicalapp.model.Prescription;
import java.util.List;

public interface PrescriptionService {

    List<Prescription> getByDoctor(String doctorEmail);

    List<Prescription> getByPatient(String patientEmail);

    Prescription create(Prescription prescription);

    // Переопределяем dispense так, чтобы первый параметр — email пациента
    boolean dispense(String patientEmail, String drugName, String dosage);
}
