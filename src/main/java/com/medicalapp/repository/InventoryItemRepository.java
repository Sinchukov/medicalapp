package com.medicalapp.repository;

import com.medicalapp.model.InventoryItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends CrudRepository<InventoryItem, Long> {

    List<InventoryItem> findAllByPharmacyId(Long pharmacyId);

    @Modifying
    @Query("""
      update InventoryItem i
      set i.quantity = i.quantity - 1
      where i.id = :itemId
        and i.quantity >= 1
      """)
    int decreaseStock(@Param("itemId") Long itemId);

    @Query("""
      select i
      from InventoryItem i
      where i.pharmacy.id = :pharmacyId
        and i.name = :name
        and i.volume = :volume
        and i.expiryDate >= :recipeExpiry
        and i.quantity >= 1
      """)
    Optional<InventoryItem> findAvailable(
            @Param("pharmacyId") Long pharmacyId,
            @Param("name") String name,
            @Param("volume") String volume,
            @Param("recipeExpiry") LocalDate recipeExpiry
    );
}
