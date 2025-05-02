package com.medicalapp.controller;

import com.medicalapp.dto.PharmacyItemDto;
import com.medicalapp.service.PharmacyItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")  // <- уникальный путь, больше не дублирует /api/pharmacy/items
@PreAuthorize("hasAuthority('PHARMACY')")
public class PharmacyItemController {

    private final PharmacyItemService service;

    public PharmacyItemController(PharmacyItemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PharmacyItemDto>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<PharmacyItemDto> add(@RequestBody PharmacyItemDto dto) {
        return ResponseEntity.ok(service.addItem(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PharmacyItemDto> update(
            @PathVariable Long id,
            @RequestBody PharmacyItemDto dto
    ) {
        return ResponseEntity.ok(service.updateItem(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
