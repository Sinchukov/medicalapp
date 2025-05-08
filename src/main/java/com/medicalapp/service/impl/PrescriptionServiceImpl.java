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
        // 1) найти рецепт
        Optional<Prescription> opt = prescriptionRepo.findById(prescriptionId);
        if (opt.isEmpty()) {
            return false;
        }
        Prescription pres = opt.get();

        // 2) проверить статус
        if (!"ISSUED".equals(pres.getStatus())) {
            return false;
        }

        // 3) попытаться списать со склада
        int updated = inventoryRepo.reduceStock(
                pres.getDrugName(),
                pres.getDosage(),
                1
        );
        if (updated != 1) {
            return false;  // не удалось списать
        }

        // 4) пометить выданным
        pres.setStatus("DISPENSED");
        // @Transactional сохранит изменения автоматически
        return true;
    }
}
