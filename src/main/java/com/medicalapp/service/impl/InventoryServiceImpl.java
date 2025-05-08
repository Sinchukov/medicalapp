// src/main/java/com/medicalapp/service/impl/InventoryServiceImpl.java
package com.medicalapp.service.impl;

import com.medicalapp.model.InventoryItem;
import com.medicalapp.repository.InventoryItemRepository;
import com.medicalapp.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository repo;

    public InventoryServiceImpl(InventoryItemRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public boolean reduceStock(String name, String volume, int count) {
        return repo.findByNameAndVolume(name, volume)
                .filter(item -> item.getQuantity() >= count)
                .map(item -> {
                    item.setQuantity(item.getQuantity() - count);
                    return true;
                })
                .orElse(false);
    }
}
