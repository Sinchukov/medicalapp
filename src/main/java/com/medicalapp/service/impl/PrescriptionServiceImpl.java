package com.medicalapp.service.impl;

import com.medicalapp.model.Prescription;
import com.medicalapp.repository.PrescriptionRepository;
import com.medicalapp.service.PrescriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository repo;

    public PrescriptionServiceImpl(PrescriptionRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Prescription> getByDoctor(String doctorEmail) {
        return repo.findAllByDoctorEmail(doctorEmail);
    }

    @Override
    public List<Prescription> getByPatient(String patientEmail) {
        return repo.findAllByPatientEmail(patientEmail);
    }

    @Override
    public Prescription create(Prescription prescription) {
        return repo.save(prescription);
    }

    @Override
    @Transactional
    public boolean dispense(String patientEmail, String drugName, String dosage) {
        Optional<Prescription> maybe =
                repo.findByPatientEmailAndDrugNameAndDosage(patientEmail, drugName, dosage);

        if (maybe.isEmpty()) {
            return false;
        }
        repo.delete(maybe.get());
        return true;
    }
}
