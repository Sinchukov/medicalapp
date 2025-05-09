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

    private final PharmacyRepository pharmacyRepo;
    private final InventoryItemRepository itemRepo;
    private final PrescriptionRepository prescriptionRepo;

    public PharmacyDispenseController(
            PharmacyRepository pharmacyRepo,
            InventoryItemRepository itemRepo,
            PrescriptionRepository prescriptionRepo
    ) {
        this.pharmacyRepo = pharmacyRepo;
        this.itemRepo = itemRepo;
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
    ){
        // 1) Найти аптеку по email из Authentication
        String email = authentication.getName();
        var phOpt = pharmacyRepo.findByEmail(email);
        if (phOpt.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("ok", false, "error", "Pharmacy not found"));
        }
        long pharmacyId = phOpt.get().getId();

        // 2) Найти рецепт
        Optional<Prescription> orx = prescriptionRepo.findById(rxId);
        if (orx.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("ok", false, "error", "Prescription not found"));
        }
        Prescription rx = orx.get();

        // 3) Уже выдан?
        if (rx.isDispensed()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("ok", false, "error", "Already dispensed"));
        }

        // 4) Проверить наличие на складе и срок годности
        Optional<InventoryItem> oi = itemRepo.findAvailable(
                pharmacyId,
                rx.getDrugName(),
                rx.getDosage(),
                rx.getExpiryDate()
        );
        if (oi.isEmpty()) {
            return ResponseEntity.ok(Map.of("ok", false, "error", "expiry_date problem"));
        }
        InventoryItem item = oi.get();

        // 5) Списать 1 шт.
        int updated = itemRepo.decreaseStock(item.getId());
        if (updated != 1) {
            return ResponseEntity.ok(Map.of("ok", false, "error", "Insufficient stock"));
        }

        // 6) Пометить рецепт как выданный
        rx.setDispensed(true);
        prescriptionRepo.save(rx);

        // 7) Ответ
        return ResponseEntity.ok(Map.of("ok", true, "status", "Recipe GivedAway"));
    }
}
