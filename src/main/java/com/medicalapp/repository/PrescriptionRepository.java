package com.medicalapp.repository;

import com.medicalapp.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findAllByPatientLogin(String login);
    List findAllByIssuedBy(String issuedBy);
    List<Prescription> findAllByDoctorEmail(String email);

    List<Prescription> findAllByPatientEmail(String email);
}
