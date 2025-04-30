// src/main/java/com/medicalapp/controller/PharmacyController.java
package com.medicalapp.controller;

import com.medicalapp.dto.InventoryItemDto;
import com.medicalapp.model.InventoryItem;
import com.medicalapp.model.Pharmacy;
import com.medicalapp.repository.InventoryItemRepository;
import com.medicalapp.repository.PharmacyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyController {
    private final PharmacyRepository phrRepo;
    private final InventoryItemRepository invRepo;

    public PharmacyController(PharmacyRepository phrRepo,
                              InventoryItemRepository invRepo) {
        this.phrRepo = phrRepo;
        this.invRepo = invRepo;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Authentication auth) {
        Pharmacy p = phrRepo.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(Map.of(
                "type",       p.getRole(),
                "email",      p.getEmail(),
                "registered", p.getRegistrationDate().toString()
        ));
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItem(
            Authentication auth,
            @RequestBody InventoryItemDto dto
    ) {
        Pharmacy p = phrRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));

        InventoryItem item = new InventoryItem();
        item.setName(dto.getName());
        item.setCountry(dto.getCountry());
        item.setQuantity(dto.getQuantity());
        item.setVolume(dto.getVolume());
        // ← Устанавливаем дату годности
        item.setExpiryDate(dto.getExpiryDate());
        item.setPharmacy(p);

        invRepo.save(item);
        return ResponseEntity.ok(Map.of("status", "created"));
    }

    @GetMapping("/items")
    public ResponseEntity<List<InventoryItemDto>> getItems(Authentication auth) {
        Pharmacy p = phrRepo.findByEmail(auth.getName()).orElseThrow();
        var list = invRepo.findAllByPharmacyId(p.getId()).stream()
                .map(item -> {
                    InventoryItemDto dto = new InventoryItemDto();
                    dto.setName(item.getName());
                    dto.setCountry(item.getCountry());
                    dto.setQuantity(item.getQuantity());
                    dto.setVolume(item.getVolume());
                    dto.setExpiryDate(item.getExpiryDate());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
