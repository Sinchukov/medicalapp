// src/main/java/com/medicalapp/service/DoctorService.java
package com.medicalapp.service;

import com.medicalapp.dto.DoctorPersonalInfoDto;
import com.medicalapp.model.Doctor;
import com.medicalapp.repository.DoctorRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorService {
    private final DoctorRepository repo;

    public DoctorService(DoctorRepository repo) {
        this.repo = repo;
    }

    public Doctor findByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Doctor not found"));
    }

    @Transactional
    public void updatePersonalInfo(String email, DoctorPersonalInfoDto dto) {
        Doctor d = findByEmail(email);
        d.setLastName(dto.getLastName());
        d.setFirstName(dto.getFirstName());
        d.setMiddleName(dto.getMiddleName());

        // Вместо двух полей – одно
        d.setPassportSeriesAndNumber(dto.getPassportSeriesAndNumber());

        d.setPassportIssueDate(dto.getPassportIssueDate());
        d.setPassportIssuedBy(dto.getPassportIssuedBy());
        d.setIdentificationNumber(dto.getIdentificationNumber());
        d.setQualification(dto.getQualification());
        d.setExperience(dto.getExperience());
        d.setWorkplace(dto.getWorkplace());

        repo.save(d);
    }
}
