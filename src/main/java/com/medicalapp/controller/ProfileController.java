package com.medicalapp.controller;

import com.medicalapp.model.Doctor;
import com.medicalapp.model.Patient;
import com.medicalapp.model.Pharmacy;
import com.medicalapp.repository.DoctorRepository;
import com.medicalapp.repository.PatientRepository;
import com.medicalapp.repository.PharmacyRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
//sd
@RestController
@RequestMapping("/auth")
public class ProfileController {

    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final PharmacyRepository pharmacyRepo;

    public ProfileController(PatientRepository patientRepo,
                             DoctorRepository doctorRepo,
                             PharmacyRepository pharmacyRepo) {
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
        this.pharmacyRepo = pharmacyRepo;
    }

    @GetMapping("/profile")
    public UserProfile getProfile(Authentication auth) {
        String email = auth.getName();
        // определяем роль
        String role = auth.getAuthorities()
                .stream()
                .findFirst()
                .map(gr -> gr.getAuthority().replace("ROLE_", ""))
                .orElse("UNKNOWN");

        // получаем дату регистрации из соответствующего репозитория
        Optional<String> optionalDate = Optional.empty();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        if ("PATIENT".equals(role)) {
            optionalDate = patientRepo.findByEmail(email)
                    .map(Patient::getRegistrationDate)
                    .map(d -> d.format(fmt));
        }
        if ("DOCTOR".equals(role)) {
            optionalDate = doctorRepo.findByEmail(email)
                    .map(Doctor::getRegistrationDate)
                    .map(d -> d.format(fmt));
        }
        if ("PHARMACY".equals(role)) {
            optionalDate = pharmacyRepo.findByEmail(email)
                    .map(Pharmacy::getRegistrationDate)
                    .map(d -> d.format(fmt));
        }

        String regDate = optionalDate.orElse("—");
        return new UserProfile(email, role, regDate);
    }

    // Вложенный DTO-класс
    public static class UserProfile {
        private final String email;
        private final String role;
        private final String registrationDate;

        public UserProfile(String email, String role, String registrationDate) {
            this.email = email;
            this.role = role;
            this.registrationDate = registrationDate;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        public String getRegistrationDate() {
            return registrationDate;
        }
    }
}
