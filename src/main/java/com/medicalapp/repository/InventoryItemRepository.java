// src/main/java/com/medicalapp/repository/InventoryItemRepository.java
package com.medicalapp.repository;

import com.medicalapp.model.InventoryItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface InventoryItemRepository extends CrudRepository<InventoryItem, Long> {
    List<InventoryItem> findAllByPharmacyId(Long pharmacyId);

    @Query("""
      select i
      from InventoryItem i
      where i.pharmacy.id = :pharmacyId
        and i.name  = :name
        and i.volume = :volume
        and i.expiryDate >= :recipeExpiry
        and i.quantity >= 1
      """)
    Optional<InventoryItem> findAvailable(
            @Param("pharmacyId") Long pharmacyId,
            @Param("name")       String name,
            @Param("volume")     String volume,
            @Param("recipeExpiry") java.time.LocalDate recipeExpiry
    );

    @Modifying
    @Query("""
      update InventoryItem i
      set i.quantity = i.quantity - 1
      where i.id = :itemId
        and i.quantity >= 1
      """)
    int decreaseStock(@Param("itemId") Long itemId);

    // ===> Добавляем эту часть:
    @Modifying
    @Query("""
      update InventoryItem i
      set i.quantity = i.quantity - :amount
      where i.name    = :name
        and i.volume  = :volume
        and i.quantity >= :amount
      """)
    int reduceStock(
            @Param("name")   String name,
            @Param("volume") String volume,
            @Param("amount") int amount
    );
}
