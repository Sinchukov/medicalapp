package com.medicalapp.repository;

import com.medicalapp.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findAllByDoctorEmail(String doctorEmail);

    List<Prescription> findAllByPatientEmail(String patientEmail);

    // ВАЖНО: ищем по patientEmail, а не по patientId
    Optional<Prescription> findByPatientEmailAndDrugNameAndDosage(
            String patientEmail,
            String drugName,
            String dosage
    );
}
