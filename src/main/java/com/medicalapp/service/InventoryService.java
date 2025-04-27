package com.medicalapp.service;

import com.medicalapp.model.InventoryItem;
import com.medicalapp.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryItemRepository itemRepo;

    public InventoryService(InventoryItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    /**
     * Возвращает все позиции инвентаря для аптеки, идентифицированной по email.
     */
    public List<InventoryItem> getItemsForPharmacy(String pharmacyEmail) {
        return itemRepo.findAllByPharmacyEmail(pharmacyEmail);
    }

    /** остальные методы (add, delete, update) остаются без изменений */
}
