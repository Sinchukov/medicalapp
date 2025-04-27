package com.medicalapp.service;

import com.medicalapp.model.Patient;
import com.medicalapp.model.Doctor;
import com.medicalapp.model.Pharmacy;
import com.medicalapp.model.Role;
import com.medicalapp.repository.PatientRepository;
import com.medicalapp.repository.DoctorRepository;
import com.medicalapp.repository.PharmacyRepository;
import com.medicalapp.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {

    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final PharmacyRepository pharmacyRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(PatientRepository patientRepo,
                       DoctorRepository doctorRepo,
                       PharmacyRepository pharmacyRepo,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider) {
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
        this.pharmacyRepo = pharmacyRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Универсальный метод регистрации.
     *
     * @param role        "PATIENT", "DOCTOR" или "PHARMACY"
     * @param email       email
     * @param password    пароль
     * @param companyName только для PHARMACY, для других — null или игнорируется
     */
    public void register(String role,
                         String email,
                         String password,
                         String companyName) {
        Role r = Role.valueOf(role.toUpperCase());
        switch (r) {
            case PATIENT:
                registerPatient(email, password);
                break;
            case DOCTOR:
                registerDoctor(email, password);
                break;
            case PHARMACY:
                registerPharmacy(email, password, companyName);
                break;
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    public void registerPatient(String email, String rawPassword) {
        Patient p = new Patient();
        p.setEmail(email);
        p.setPassword(passwordEncoder.encode(rawPassword));
        p.setRegistrationDate(LocalDate.now());
        p.setRole(Role.PATIENT);
        patientRepo.save(p);
    }

    public void registerDoctor(String email, String rawPassword) {
        Doctor d = new Doctor();
        d.setEmail(email);
        d.setPassword(passwordEncoder.encode(rawPassword));
        d.setRegistrationDate(LocalDate.now());
        d.setRole(Role.DOCTOR);
        doctorRepo.save(d);
    }

    public void registerPharmacy(String email,
                                 String rawPassword,
                                 String companyName) {
        Pharmacy ph = new Pharmacy();
        ph.setEmail(email);
        ph.setPassword(passwordEncoder.encode(rawPassword));
        ph.setCompanyName(companyName);
        ph.setRegistrationDate(LocalDate.now());
        ph.setRole(Role.PHARMACY);
        pharmacyRepo.save(ph);
    }

    /**
     * Аутентификация пользователя и генерация JWT.
     *
     * @param email    email
     * @param password пароль
     * @return JWT-токен
     */
    public String login(String email, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        return jwtTokenProvider.generateToken(String.valueOf(auth));
    }
}
