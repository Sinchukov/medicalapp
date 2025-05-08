// src/main/java/com/medicalapp/service/impl/InventoryServiceImpl.java
package com.medicalapp.service.impl;

import com.medicalapp.model.InventoryItem;
import com.medicalapp.repository.InventoryItemRepository;
import com.medicalapp.service.InventoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository repo;

    public InventoryServiceImpl(InventoryItemRepository repo) {
        this.repo = repo;
    }

    public Optional<InventoryItem> findAvailable(
            Long pharmacyId, String name, String volume, LocalDate recipeExpiry
    ) {
        return repo.findAvailable(pharmacyId, name, volume, recipeExpiry);
    }

    public boolean decreaseOne(Long itemId) {
        return repo.decreaseStock(itemId) == 1;
    }
}
