package com.medicalapp.service;

import com.medicalapp.dto.RegisterDto;
import com.medicalapp.model.*;
import com.medicalapp.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class AuthService {

    private final PatientRepository   pr;
    private final DoctorRepository    dr;
    private final PharmacyRepository  phr;
    private final BCryptPasswordEncoder enc;

    public AuthService(PatientRepository pr,
                       DoctorRepository dr,
                       PharmacyRepository phr,
                       BCryptPasswordEncoder enc) {
        this.pr  = pr;
        this.dr  = dr;
        this.phr = phr;
        this.enc = enc;
    }

    public void register(RegisterDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Passwords mismatch");
        }
        String hashed = enc.encode(dto.getPassword());
        LocalDate now = LocalDate.now();

        switch (dto.getRole()) {
            case "PATIENT":
                Patient p = new Patient();
                p.setEmail(dto.getEmail());
                p.setPassword(hashed);
                p.setRole(Role.PATIENT);
                p.setRegistrationDate(now);
                pr.save(p);
                break;
            case "DOCTOR":
                Doctor d = new Doctor();
                d.setEmail(dto.getEmail());
                d.setPassword(hashed);
                d.setRole(Role.DOCTOR);
                d.setRegistrationDate(now);
                dr.save(d);
                break;
            case "PHARMACY":
                Pharmacy ph = new Pharmacy();
                ph.setEmail(dto.getEmail());
                ph.setPassword(hashed);
                ph.setRole(Role.PHARMACY);
                ph.setCompanyName(dto.getCompanyName());
                ph.setRegistrationDate(now);
                phr.save(ph);
                break;
            default:
                throw new RuntimeException("Unknown role");
        }
    }
}
