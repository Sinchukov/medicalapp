// src/main/java/com/medicalapp/controller/PharmacyController.java
package com.medicalapp.controller;

import com.medicalapp.dto.InventoryItemDto;
import com.medicalapp.model.InventoryItem;
import com.medicalapp.model.Pharmacy;
import com.medicalapp.repository.InventoryItemRepository;
import com.medicalapp.repository.PharmacyRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyController {

    private final PharmacyRepository phrRepo;
    private final InventoryItemRepository invRepo;
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public PharmacyController(PharmacyRepository phrRepo,
                              InventoryItemRepository invRepo) {
        this.phrRepo = phrRepo;
        this.invRepo = invRepo;
    }

    // 1) профиль аптеки
    @GetMapping("/profile")
    public ResponseEntity<?> profile(Authentication auth) {
        Pharmacy p = phrRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));
        Map<String, String> body = Map.of(
                "type",        p.getRole().name(),
                "companyName", p.getCompanyName(),
                "email",       p.getEmail(),
                "registered",  p.getRegistrationDate().format(DMY)
        );
        return ResponseEntity.ok(body);
    }

    // 2) товары аптеки
    @GetMapping("/items")
    public ResponseEntity<List<InventoryItem>> getItems(Authentication auth) {
        Pharmacy p = phrRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));
        List<InventoryItem> items = invRepo.findAllByPharmacyId(p.getId());
        return ResponseEntity.ok(items);
    }

    // 3) добавить товар
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
        item.setVolume(dto.getVolume());
        item.setQuantity(dto.getQuantity());
        item.setExpiryDate(dto.getExpiryDate());
        item.setPharmacy(p);

        invRepo.save(item);
        return ResponseEntity.ok(Map.of("status", "created"));
    }
}
