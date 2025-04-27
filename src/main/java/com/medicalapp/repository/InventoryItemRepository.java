// src/main/java/com/medicalapp/repository/InventoryItemRepository.java
package com.medicalapp.repository;

import com.medicalapp.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryItemRepository extends JpaRepository<InventoryItem,Long> {
    List<InventoryItem> findAllByPharmacyEmail(String email);
    List<InventoryItem> findAllByPharmacyId(Long pharmacyId);
}
