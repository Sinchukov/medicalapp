package com.medicalapp.service;

import com.medicalapp.model.Prescription;
import com.medicalapp.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {
    private final PrescriptionRepository repo;

    public PrescriptionService(PrescriptionRepository repo) {
        this.repo = repo;
    }

    public List<Prescription> getByPatient(String patientEmail) {
        return repo.findAllByPatientEmail(patientEmail);
    }

    public List<Prescription> getByDoctor(String doctorEmail) {
        return repo.findAllByDoctorEmail(doctorEmail);
    }
}