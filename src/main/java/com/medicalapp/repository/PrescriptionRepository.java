// src/main/java/com/medicalapp/repository/PrescriptionRepository.java
package com.medicalapp.repository;

import com.medicalapp.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findAllByPatientEmail(String patientEmail);
    List<Prescription> findAllByDoctorEmail(String doctorEmail);
}
