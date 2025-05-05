// src/main/java/com/medicalapp/service/impl/InventoryServiceImpl.java

package com.medicalapp.service.impl;

import com.medicalapp.model.InventoryItem;
import com.medicalapp.repository.InventoryItemRepository;
import com.medicalapp.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository repo;

    public InventoryServiceImpl(InventoryItemRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean reduceStock(String drugName, String volume, int qty) {
        // раньше findByNameAndDosage → теперь findByNameAndVolume
        Optional<InventoryItem> maybe = repo.findByNameAndVolume(drugName, volume);
        if (maybe.isEmpty()) {
            return false;
        }
        InventoryItem item = maybe.get();
        if (item.getQuantity() < qty) {
            return false;
        }
        item.setQuantity(item.getQuantity() - qty);
        repo.save(item);
        return true;
    }
}
