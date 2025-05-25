package com.medicalapp.controller;

import com.medicalapp.model.InventoryItem;
import com.medicalapp.model.Prescription;
import com.medicalapp.repository.InventoryItemRepository;
import com.medicalapp.repository.PharmacyRepository;
import com.medicalapp.repository.PrescriptionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyDispenseController {


    private final PharmacyRepository    pharmacyRepo;
    private final InventoryItemRepository itemRepo;
    private final PrescriptionRepository prescriptionRepo;

    public PharmacyDispenseController(
            PharmacyRepository pharmacyRepo,
            InventoryItemRepository itemRepo,
            PrescriptionRepository prescriptionRepo
    ) {
        this.pharmacyRepo     = pharmacyRepo;
        this.itemRepo         = itemRepo;
        this.prescriptionRepo = prescriptionRepo;
    }

    /**
     * Выдача одного рецепта. Расширенные проверки ошибок.
     */
    @PreAuthorize("hasRole('PHARMACY')")
    @PostMapping("/prescriptions/{rxId}/dispense")
    @Transactional
    public ResponseEntity<?> dispense(
            Authentication authentication,
            @PathVariable("rxId") Long rxId
    ) {
        // 1) найдём аптеку
        String email = authentication.getName();
        var phOpt = pharmacyRepo.findByEmail(email);
        if (phOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("ok", false, "error", "Лекарство не найдено"));
        }
        long pharmacyId = phOpt.get().getId();

        // 2) найдём рецепт
        Optional<Prescription> orx = prescriptionRepo.findById(rxId);
        if (orx.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("ok", false, "error", "Рецепт не найден"));
        }
        Prescription rx = orx.get();

        // 3) уже выдан?
        if ("DISPENSED".equalsIgnoreCase(rx.getStatus())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("ok", false, "error", "Уже выдан"));
        }

        String drugName = rx.getDrugName();
        String dosage   = rx.getDosage();

        // 4a) проверяем наличие названия
        boolean hasName = itemRepo.existsByPharmacyIdAndName(pharmacyId, drugName);
        if (!hasName) {
            return ResponseEntity.ok(Map.of("ok", false, "error", "Лекарство не найдено"));
        }

        // 4b) проверяем дозировку
        boolean hasVolume = itemRepo.existsByPharmacyIdAndNameAndVolume(pharmacyId, drugName, dosage);
        if (!hasVolume) {
            return ResponseEntity.ok(Map.of("ok", false, "error", "Нет соответствущей дозировки лекарства"));
        }

        // 4c) проверяем срок годности и количество
        Optional<InventoryItem> oi = itemRepo.findAvailable(
                pharmacyId,
                drugName,
                dosage,
                rx.getExpiryDate()
        );
        if (oi.isEmpty()) {
            return ResponseEntity.ok(Map.of("ok", false, "error", "Срок действия истек"));
        }
        InventoryItem item = oi.get();

        // 5) списываем упаковку
        int updated = itemRepo.decreaseStock(item.getId());
        if (updated != 1) {
            return ResponseEntity.ok(Map.of("ok", false, "error", "Недостаточное количество упаковок в наличии"));
        }

        // 6) помечаем рецепт
        rx.setStatus("DISPENSED");
        prescriptionRepo.save(rx);

        // 7) возвращаем успех
        return ResponseEntity.ok(Map.of("ok", true, "status", "Рецепт выдан!!!"));
    }
}
