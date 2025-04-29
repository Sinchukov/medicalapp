// src/main/java/com/medicalapp/controller/ProfileController.java
package com.medicalapp.controller;

import com.medicalapp.model.Patient;
import com.medicalapp.model.Doctor;
import com.medicalapp.model.Pharmacy;
import com.medicalapp.repository.PatientRepository;
import com.medicalapp.repository.DoctorRepository;
import com.medicalapp.repository.PharmacyRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final PharmacyRepository pharmacyRepo;

    public ProfileController(PatientRepository patientRepo,
                             DoctorRepository doctorRepo,
                             PharmacyRepository pharmacyRepo) {
        this.patientRepo  = patientRepo;
        this.doctorRepo   = doctorRepo;
        this.pharmacyRepo = pharmacyRepo;
    }

    @GetMapping
    public UserProfile getProfile(Authentication auth) {
        String email = auth.getName();
        String role  = auth.getAuthorities().stream()
                .findFirst().orElseThrow()
                .getAuthority().replace("ROLE_", "");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String regDate = "—";

        // retrieve registration date based on role
        if ("PATIENT".equals(role)) {
            regDate = patientRepo.findByEmail(email)
                    .map(Patient::getRegistrationDate)
                    .map(d -> d.format(fmt))
                    .orElse("—");
        } else if ("DOCTOR".equals(role)) {
            regDate = doctorRepo.findByEmail(email)
                    .map(Doctor::getRegistrationDate)
                    .map(d -> d.format(fmt))
                    .orElse("—");
        } else if ("PHARMACY".equals(role)) {
            regDate = pharmacyRepo.findByEmail(email)
                    .map(Pharmacy::getRegistrationDate)
                    .map(d -> d.format(fmt))
                    .orElse("—");
        }

        return new UserProfile(email, role, regDate);
    }

    public static class UserProfile {
        private final String email;
        private final String role;
        private final String registrationDate;

        public UserProfile(String email, String role, String registrationDate) {
            this.email = email;
            this.role = role;
            this.registrationDate = registrationDate;
        }
        public String getEmail() { return email; }
        public String getRole()  { return role; }
        public String getRegistrationDate() { return registrationDate; }
    }
}