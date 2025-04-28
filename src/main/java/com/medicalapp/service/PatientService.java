// src/main/java/com/medicalapp/service/PatientService.java
package com.medicalapp.service;

import com.medicalapp.dto.CheckPatientDto;
import com.medicalapp.dto.PatientPersonalInfoDto;
import com.medicalapp.model.Patient;
import com.medicalapp.repository.PatientRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientService {
    private final PatientRepository repo;

    public PatientService(PatientRepository repo) {
        this.repo = repo;
    }

    public Patient findByEmail(String email) throws Throwable {
        return (Patient) repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Patient not found"));
    }
    public Patient findByPersonalData(CheckPatientDto d) {
        return repo.findByLastNameAndFirstNameAndMiddleNameAndPassportSeriesAndPassportNumberAndPassportIssueDateAndPassportIssuedByAndIdentificationNumber(
                d.getLastName(), d.getFirstName(), d.getMiddleName(),
                d.getPassportSeries(), d.getPassportNumber(),
                d.getPassportIssueDate(), d.getPassportIssuedBy(),
                d.getIdentificationNumber()
        ).orElse(null);
    }
    @Transactional
    public void updatePersonalInfo(String email, PatientPersonalInfoDto dto) throws Throwable {
        Patient p = findByEmail(email);
        p.setLastName(dto.getLastName());
        p.setFirstName(dto.getFirstName());
        p.setMiddleName(dto.getMiddleName());
        p.setPassportSeries(dto.getPassportSeries());
        p.setPassportNumber(dto.getPassportNumber());
        p.setPassportIssueDate(dto.getPassportIssueDate());
        p.setPassportIssuedBy(dto.getPassportIssuedBy());
        p.setIdentificationNumber(dto.getIdentificationNumber());
        repo.save(p);
    }
}
