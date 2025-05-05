package com.medicalapp.service;

import com.medicalapp.model.Prescription;

import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    List<Prescription> getByDoctor(String doctorEmail);
    List<Prescription> getByPatient(String patientEmail);
    Prescription create(Prescription prescription);

    // новинки:
    Optional<Prescription> findById(Long id);
    void markDispensed(Long id);
}
