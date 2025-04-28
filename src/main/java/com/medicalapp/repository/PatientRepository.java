// src/main/java/com/medicalapp/repository/PatientRepository.java
package com.medicalapp.repository;

import com.medicalapp.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByLastNameAndFirstNameAndMiddleNameAndPassportSeriesAndPassportNumberAndPassportIssueDateAndPassportIssuedByAndIdentificationNumber(
            String lastName,
            String firstName,
            String middleName,
            String passportSeries,
            String passportNumber,
            LocalDate passportIssueDate,
            String passportIssuedBy,
            String identificationNumber
    );
}