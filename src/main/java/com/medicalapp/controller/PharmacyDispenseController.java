package com.medicalapp.controller;

import com.medicalapp.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyDispenseController {

    private final PrescriptionService prescriptionService;

    public PharmacyDispenseController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PreAuthorize("hasAuthority('PHARMACY')")
    @PostMapping("/prescriptions/{id}/dispense")
    public ResponseEntity<?> dispense(@PathVariable Long id) {
        // просто помечаем выданным
        prescriptionService.dispenseAndReduceStock(id);
        return ResponseEntity.ok(Map.of("status","dispensed"));
    }
}
