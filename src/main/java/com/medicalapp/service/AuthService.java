package com.medicalapp.service;

import com.medicalapp.dto.RegisterDto;
import com.medicalapp.model.Doctor;
import com.medicalapp.model.Patient;
import com.medicalapp.model.Pharmacy;
import com.medicalapp.model.Role;
import com.medicalapp.repository.DoctorRepository;
import com.medicalapp.repository.PatientRepository;
import com.medicalapp.repository.PharmacyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {

    private final PatientRepository   patientRepo;
    private final DoctorRepository    doctorRepo;
    private final PharmacyRepository  pharmacyRepo;
    private final PasswordEncoder     passwordEncoder;

    public AuthService(
            PatientRepository patientRepo,
            DoctorRepository doctorRepo,
            PharmacyRepository pharmacyRepo,
            PasswordEncoder passwordEncoder
    ) {
        this.patientRepo     = patientRepo;
        this.doctorRepo      = doctorRepo;
        this.pharmacyRepo    = pharmacyRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Регистрирует нового User (Patient / Doctor / Pharmacy)
     * @param dto – dto с полями email, password, confirmPassword, role, companyName (для аптеки)
     */
    public void register(RegisterDto dto) {
        // 1) проверка совпадения паролей
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Passwords mismatch");
        }

        // 2) хешируем
        String hashed = passwordEncoder.encode(dto.getPassword());
        LocalDate now = LocalDate.now();

        // 3) создаём нужную сущность в зависимости от роли
        switch (dto.getRole()) {
            case "PATIENT":
                Patient p = new Patient();
                p.setEmail(dto.getEmail());
                p.setPassword(hashed);
                p.setRole(Role.PATIENT);
                p.setRegistrationDate(now);
                patientRepo.save(p);
                break;

            case "DOCTOR":
                Doctor d = new Doctor();
                d.setEmail(dto.getEmail());
                d.setPassword(hashed);
                d.setRole(Role.DOCTOR);
                d.setRegistrationDate(now);
                doctorRepo.save(d);
                break;

            case "PHARMACY":
                Pharmacy ph = new Pharmacy();
                ph.setEmail(dto.getEmail());
                ph.setPassword(hashed);
                ph.setRole(Role.PHARMACY);
                ph.setCompanyName(dto.getCompanyName());
                ph.setRegistrationDate(now);
                pharmacyRepo.save(ph);
                break;

            default:
                throw new RuntimeException("Unknown role: " + dto.getRole());
        }
    }
}
