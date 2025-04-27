package com.medicalapp.controller;

import com.medicalapp.dto.InventoryItemDto;
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

    @PostMapping("/items")
    public ResponseEntity<?> addItem(
            Authentication auth,
            @RequestBody InventoryItemDto dto) {

        Pharmacy p = phrRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));

        InventoryItem item = new InventoryItem();
        item.setName(dto.getName());
        item.setCountry(dto.getCountry());
        item.setQuantity(dto.getQuantity());
        item.setVolume(dto.getVolume());
        item.setPharmacy(p);

        invRepo.save(item);
        return ResponseEntity.ok(Map.of("status", "created"));
    }

    @GetMapping("/items")
    public ResponseEntity<List<InventoryItem>> getItems(Authentication auth) {
        Pharmacy p = phrRepo.findByEmail(auth.getName())
                .orElseThrow();
        // Метод в репозитории должен быть:
        // List<InventoryItem> findAllByPharmacyId(Long pharmacyId);
        return ResponseEntity.ok(invRepo.findAllByPharmacyId(p.getId()));
    }
}
