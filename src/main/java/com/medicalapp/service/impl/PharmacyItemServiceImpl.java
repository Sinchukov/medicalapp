// src/main/java/com/medicalapp/service/impl/PharmacyItemServiceImpl.java
package com.medicalapp.service.impl;

import com.medicalapp.dto.PharmacyItemDto;
import com.medicalapp.model.InventoryItem;
import com.medicalapp.repository.InventoryItemRepository;
import com.medicalapp.service.PharmacyItemService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PharmacyItemServiceImpl implements PharmacyItemService {

    private final InventoryItemRepository repo;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;

    public PharmacyItemServiceImpl(InventoryItemRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<PharmacyItemDto> findAll() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PharmacyItemDto addItem(PharmacyItemDto dto) {
        InventoryItem item = new InventoryItem();
        item.setName(dto.getName());
        item.setCountry(dto.getCountry());
        item.setQuantity(dto.getQuantity());
        // вместо dto.getDosage() — dto.getVolume()
        item.setVolume(dto.getVolume());
        // строка yyyy-MM-dd → LocalDate
        item.setExpiryDate(LocalDate.parse(dto.getExpiryDate(), ISO));

        InventoryItem saved = repo.save(item);
        return toDto(saved);
    }

    @Override
    public PharmacyItemDto updateItem(Long id, PharmacyItemDto dto) {
        InventoryItem item = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setName(dto.getName());
        item.setCountry(dto.getCountry());
        item.setQuantity(dto.getQuantity());
        item.setVolume(dto.getVolume());
        item.setExpiryDate(LocalDate.parse(dto.getExpiryDate(), ISO));

        InventoryItem saved = repo.save(item);
        return toDto(saved);
    }

    @Override
    public void deleteItem(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Item not found");
        }
        repo.deleteById(id);
    }

    private PharmacyItemDto toDto(InventoryItem item) {
        PharmacyItemDto dto = new PharmacyItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setCountry(item.getCountry());
        dto.setQuantity(item.getQuantity());
        // вместо setDosage → setVolume
        dto.setVolume(item.getVolume());
        dto.setExpiryDate(item.getExpiryDate().format(ISO));
        return dto;
    }
}
