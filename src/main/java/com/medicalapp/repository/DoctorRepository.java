// src/main/java/com/medicalapp/repository/DoctorRepository.java
package com.medicalapp.repository;

import com.medicalapp.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(String email);
}