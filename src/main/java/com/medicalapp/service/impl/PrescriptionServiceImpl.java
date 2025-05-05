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
        // при создании рецепта сразу выставляем статус ISSUED
        prescription.setStatus("ISSUED");
        return repo.save(prescription);
    }

    @Override
    public Optional<Prescription> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    @Transactional
    public void markDispensed(Long id) {
        repo.findById(id).ifPresent(p -> {
            p.setStatus("DISPENSED");
            // благодаря @Transactional — в конце метода JPA подхватит и сохранит изменение
        });
    }
}
