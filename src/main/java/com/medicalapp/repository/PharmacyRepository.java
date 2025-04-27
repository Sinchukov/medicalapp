// src/main/java/com/medicalapp/repository/PharmacyRepository.java
package com.medicalapp.repository;

import com.medicalapp.model.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PharmacyRepository extends JpaRepository<Pharmacy,Long> {
    Optional<Pharmacy> findByEmail(String email);
}
