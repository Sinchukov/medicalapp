// src/main/java/com/medicalapp/service/PatientService.java
package com.medicalapp.service;

import com.medicalapp.model.Patient;
import com.medicalapp.repository.PatientRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PatientService {
    private final PatientRepository repo;
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public PatientService(PatientRepository repo) {
        this.repo = repo;
    }

    public Patient findByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Patient not found"));
    }

    public Patient findByPersonalData(
            String lastName, String firstName, String middleName,
            String series, String number, String issueDateStr,
            String issuedBy, String idNumber
    ) {
        LocalDate issue = null;
        if (issueDateStr != null && !issueDateStr.isBlank()) {
            issue = LocalDate.parse(issueDateStr, DMY);
        }
        return repo.findByLastNameAndFirstNameAndMiddleNameAndPassportSeriesAndPassportNumberAndPassportIssueDateAndPassportIssuedByAndIdentificationNumber(
                lastName, firstName, middleName,
                series, number, issue, issuedBy, idNumber
        ).orElse(null);
    }

    @Transactional
    public void updatePersonalInfo(
            String email,
            String lastName, String firstName, String middleName,
            String series, String number, String issueDateStr,
            String issuedBy, String idNumber
    ) {
        Patient p = findByEmail(email);
        p.setLastName(lastName);
        p.setFirstName(firstName);
        p.setMiddleName(middleName);
        p.setPassportSeries(series);
        p.setPassportNumber(number);
        p.setPassportIssueDateStr(issueDateStr); // <- строковый сеттер
        p.setPassportIssuedBy(issuedBy);
        p.setIdentificationNumber(idNumber);
        repo.save(p);
    }
}
