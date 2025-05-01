package com.medicalapp.repository;

import com.medicalapp.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);

    @Query("""
    select p 
    from Patient p 
    where p.lastName = :lastName
      and p.firstName = :firstName
      and p.middleName = :middleName
      and p.passportSeriesAndNumber = :psn
      and p.passportIssueDate = :issueDate
      and p.passportIssuedBy = :issuedBy
      and p.identificationNumber = :idNumber
    """)
    Optional<Patient> findByPersonalData(
            @Param("lastName") String lastName,
            @Param("firstName") String firstName,
            @Param("middleName") String middleName,
            @Param("psn") String passportSeriesAndNumber,
            @Param("issueDate") LocalDate passportIssueDate,
            @Param("issuedBy") String passportIssuedBy,
            @Param("idNumber") String identificationNumber
    );
}
