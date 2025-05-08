// src/main/java/com/medicalapp/repository/InventoryItemRepository.java
package com.medicalapp.repository;

import com.medicalapp.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findAllByPharmacyId(Long pharmacyId);
    Optional<InventoryItem> findByNameAndVolume(String name, String volume);

    @Modifying
    @Query("UPDATE InventoryItem i " +
            "SET i.quantity = i.quantity - :amount " +
            "WHERE i.name = :name " +
            "  AND i.volume = :volume " +
            "  AND i.quantity >= :amount")
    int decreaseStock(
            @Param("name") String name,
            @Param("volume") String volume,
            @Param("amount") int amount);
    default boolean reduceStock(String name, String volume, int amount) {
        return decreaseStock(name, volume, amount) > 0;
    }
}
