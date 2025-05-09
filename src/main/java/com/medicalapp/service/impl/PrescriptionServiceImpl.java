package com.medicalapp.service.impl;

import com.medicalapp.model.InventoryItem;
import com.medicalapp.model.Prescription;
import com.medicalapp.repository.InventoryItemRepository;
import com.medicalapp.repository.PrescriptionRepository;
import com.medicalapp.service.PrescriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    /**
     * 1) Найти рецепт по ID
     * 2) Убедиться, что статус ISSUED
     * 3) Найти через findAvailable(...) подходящий InventoryItem
     * 4) Вызвать decreaseStock(itemId)
     * 5) Если stock списался, пометить рецепт DISPENSED
     */
    @Override
    @Transactional
    public boolean dispenseAndReduceStock(Long prescriptionId) {
        // 1) найти рецепт
        Optional<Prescription> optPres = prescriptionRepo.findById(prescriptionId);
        if (optPres.isEmpty()) {
            return false;
        }
        Prescription pres = optPres.get();

        // 2) только из статуса ISSUED можно выдать
        if (!"ISSUED".equals(pres.getStatus())) {
            return false;
        }

        // 3) ищем в инвентаре товар, подходящий по критериям
        Optional<InventoryItem> optItem = inventoryRepo.findAvailable(
                /* pharmacyId */ // сюда должен быть ваш способ получения pharmacyId
                // Если prescription хранит pharmacyId — берём его, иначе нужно передавать pharmacyId в метод.
                // Но в текущем сервисе pharmacyId не доступен, поэтому
                // логика dispenseAndReduceStock должна быть вызвана из контроллера,
                // где вы заранее знаете pharmacyId и вызываете репозиторий напрямую.
                0L,
                pres.getDrugName(),
                pres.getDosage(),
                pres.getExpiryDate()
        );

        if (optItem.isEmpty()) {
            return false;
        }

        InventoryItem item = optItem.get();

        // 4) списываем одну единицу
        int updatedRows = inventoryRepo.decreaseStock(item.getId());
        if (updatedRows != 1) {
            return false;
        }

        // 5) отмечаем рецепт выданным и сохраняем
        pres.setStatus("DISPENSED");
        // благодаря @Transactional обновление состояния pres в БД произойдёт автоматически
        return true;
    }
}
