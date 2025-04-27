package com.medicalapp.controller;

import com.medicalapp.dto.DoctorPersonalInfoDto;
import com.medicalapp.dto.DoctorProfileDto;
import com.medicalapp.model.Doctor;
import com.medicalapp.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/profile")
    public ResponseEntity<DoctorProfileDto> profile(Authentication auth) {
        Doctor d = doctorService.findByEmail(auth.getName());
        String registered = d.getRegistrationDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return ResponseEntity.ok(
                new DoctorProfileDto(d.getEmail(), registered, d.getRole().name())
        );
    }

    @GetMapping("/personal-info")
    public ResponseEntity<DoctorPersonalInfoDto> getInfo(Authentication auth) {
        Doctor d = doctorService.findByEmail(auth.getName());
        return ResponseEntity.ok(new DoctorPersonalInfoDto(
                d.getLastName(), d.getFirstName(), d.getMiddleName(),
                d.getPassportSeries(), d.getPassportNumber(),
                d.getPassportIssueDate(), d.getPassportIssuedBy(),
                d.getIdentificationNumber(),
                d.getQualification(), d.getExperience(), d.getWorkplace()
        ));
    }

    @PostMapping("/personal-info")
    public ResponseEntity<Map<String,String>> saveInfo(
            @RequestBody DoctorPersonalInfoDto dto,
            Authentication auth) {
        doctorService.updatePersonalInfo(auth.getName(), dto);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
