// src/main/java/com/medicalapp/repository/InventoryItemRepository.java

package com.medicalapp.repository;

import com.medicalapp.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    // для получения всех товаров аптеки
    List<InventoryItem> findAllByPharmacyId(Long pharmacyId);

    // для уменьшения запаса по названию и объёму
    Optional<InventoryItem> findByNameAndVolume(String name, String volume);
}
