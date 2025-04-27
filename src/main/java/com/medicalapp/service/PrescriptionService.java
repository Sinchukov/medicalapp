package com.medicalapp.service;

import com.medicalapp.model.Prescription;
import com.medicalapp.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<Prescription> byPatient(String email) {
        return prescriptionRepository.findAllByPatientEmail(email);
    }

    public List<Prescription> byDoctor(String email) {
        return prescriptionRepository.findAllByDoctorEmail(email);
    }
}
