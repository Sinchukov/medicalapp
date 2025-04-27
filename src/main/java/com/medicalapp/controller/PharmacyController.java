package com.medicalapp.controller;

import com.medicalapp.model.*;
import com.medicalapp.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyController {
    private final PharmacyRepository phrRepo;
    private final InventoryItemRepository invRepo;

    public PharmacyController(PharmacyRepository p, InventoryItemRepository i){
        this.phrRepo=p; this.invRepo=i;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Authentication auth){
        Pharmacy p = phrRepo.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(Map.of(
                "type", p.getCompanyName(),
                "email",p.getEmail(),
                "registered",p.getRegistrationDate().toString()
        ));
    }

    @GetMapping("/items")
    public ResponseEntity<List<InventoryItem>> getItems(Authentication auth){
        Pharmacy p = phrRepo.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(invRepo.findAllByPharmacyId(p.getId()));
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItem(Authentication auth,
                                     @RequestBody InventoryItem dto){
        Pharmacy p = phrRepo.findByEmail(auth.getName()).orElseThrow();
        dto.setPharmacy(p);
        invRepo.save(dto);
        return ResponseEntity.ok(Map.of("status","created"));
    }
}
