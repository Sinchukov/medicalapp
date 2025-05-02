// src/main/java/com/medicalapp/repository/PharmacyItemRepository.java
package com.medicalapp.repository;

import com.medicalapp.model.PharmacyItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PharmacyItemRepository extends JpaRepository<PharmacyItem, Long> {
    // по желанию: поиск по имени
    Optional<PharmacyItem> findByName(String name);

}
