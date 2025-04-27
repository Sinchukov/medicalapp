package com.medicalapp.security;

import com.medicalapp.model.Patient;
import com.medicalapp.model.Doctor;
import com.medicalapp.model.Pharmacy;
import com.medicalapp.repository.PatientRepository;
import com.medicalapp.repository.DoctorRepository;
import com.medicalapp.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final PharmacyRepository pharmacyRepo;

    @Autowired
    public UserDetailsServiceImpl(PatientRepository patientRepo,
                                  DoctorRepository doctorRepo,
                                  PharmacyRepository pharmacyRepo) {
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
        this.pharmacyRepo = pharmacyRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Patient patient = patientRepo.findByEmail(email).orElse(null);
        if (patient != null) {
            return User.builder()
                    .username(patient.getEmail())
                    .password(patient.getPassword())
                    .authorities(new SimpleGrantedAuthority("ROLE_PATIENT"))
                    .build();
        }

        Doctor doctor = doctorRepo.findByEmail(email).orElse(null);
        if (doctor != null) {
            return User.builder()
                    .username(doctor.getEmail())
                    .password(doctor.getPassword())
                    .authorities(new SimpleGrantedAuthority("ROLE_DOCTOR"))
                    .build();
        }

        Pharmacy pharmacy = pharmacyRepo.findByEmail(email).orElse(null);
        if (pharmacy != null) {
            return User.builder()
                    .username(pharmacy.getEmail())
                    .password(pharmacy.getPassword())
                    .authorities(new SimpleGrantedAuthority("ROLE_PHARMACY"))
                    .build();
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
