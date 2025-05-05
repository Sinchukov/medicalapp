package com.medicalapp.service;

import com.medicalapp.dto.PharmacyItemDto;

import java.util.List;

public interface PharmacyItemService {
    List<PharmacyItemDto> findAll();
    PharmacyItemDto addItem(PharmacyItemDto dto);
    PharmacyItemDto updateItem(Long id, PharmacyItemDto dto);
    void deleteItem(Long id);
}
