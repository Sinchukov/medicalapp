package com.medicalapp.controller;

import com.medicalapp.service.InventoryService;
import com.medicalapp.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyDispenseController {
    private final PrescriptionService prescriptionService;
    private final InventoryService inventoryService;

    public PharmacyDispenseController(
            PrescriptionService prescriptionService,
            InventoryService inventoryService
    ) {
        this.prescriptionService = prescriptionService;
        this.inventoryService = inventoryService;
    }

    @PreAuthorize("hasAuthority('PHARMACY')")
    @PostMapping("/prescriptions/{id}/dispense")
    public ResponseEntity<?> dispense(@PathVariable Long id) {
        var maybe = prescriptionService.findById(id);
        if (maybe.isEmpty() || !"ISSUED".equals(maybe.get().getStatus())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid request or already dispensed"));
        }
        var pres = maybe.get();

        boolean ok = inventoryService.reduceStock(
                pres.getDrugName(),
                pres.getDosage(),
                1
        );
        if (!ok) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Insufficient stock"));
        }

        prescriptionService.markDispensed(id);
        return ResponseEntity.ok(Map.of("status","dispensed"));
    }
}
