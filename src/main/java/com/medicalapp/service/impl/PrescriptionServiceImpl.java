// src/main/java/com/medicalapp/service/impl/PrescriptionServiceImpl.java
package com.medicalapp.service.impl;

import com.medicalapp.model.Prescription;
import com.medicalapp.repository.InventoryItemRepository;
import com.medicalapp.repository.PrescriptionRepository;
import com.medicalapp.service.PrescriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepo;
    private final InventoryItemRepository inventoryRepo;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepo,
                                   InventoryItemRepository inventoryRepo) {
        this.prescriptionRepo = prescriptionRepo;
        this.inventoryRepo = inventoryRepo;
    }

    @Override
    public List<Prescription> getByDoctor(String doctorEmail) {
        return prescriptionRepo.findAllByDoctorEmail(doctorEmail);
    }

    @Override
    public List<Prescription> getByPatient(String patientEmail) {
        return prescriptionRepo.findAllByPatientEmail(patientEmail);
    }

    @Override
    public Prescription create(Prescription prescription) {
        if (prescription.getStatus() == null) {
            prescription.setStatus("ISSUED");
        }
        return prescriptionRepo.save(prescription);
    }

    @Override
    public Optional<Prescription> findById(Long id) {
        return prescriptionRepo.findById(id);
    }

    @Override
    @Transactional
    public boolean dispenseAndReduceStock(Long prescriptionId) {
        // 1) Найти рецепт
        Optional<Prescription> opt = prescriptionRepo.findById(prescriptionId);
        if (opt.isEmpty()) {
            return false;
        }
        Prescription pres = opt.get();

        // 2) Проверить, что ещё не выдан
        if (!"ISSUED".equals(pres.getStatus())) {
            return false;
        }

        // 3) Уменьшить на складе (1 штука)
        boolean stockOk = inventoryRepo.reduceStock(
                pres.getDrugName(),
                pres.getDosage(),
                1
        );
        if (!stockOk) {
            return false;
        }

        // 4) Пометить рецепт как выданный
        pres.setStatus("DISPENSED");
        // благодаря @Transactional сохранится автоматически
        return true;
    }
}
