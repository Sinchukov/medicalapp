// src/main/java/com/medicalapp/repository/PatientRepository.java
package com.medicalapp.repository;

import com.medicalapp.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    Optional<Patient> findByEmail(String email);
}
