package com.medicalapp.controller;

import com.medicalapp.dto.PatientPersonalInfoDto;
import com.medicalapp.dto.PatientProfileDto;
import com.medicalapp.model.Patient;
import com.medicalapp.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/profile")
    public ResponseEntity<PatientProfileDto> profile(Authentication auth) {
        Patient p = patientService.findByEmail(auth.getName());
        String registered = p.getRegistrationDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return ResponseEntity.ok(
                new PatientProfileDto(p.getEmail(), registered, p.getRole().name())
        );
    }

    @GetMapping("/personal-info")
    public ResponseEntity<PatientPersonalInfoDto> getInfo(Authentication auth) {
        Patient p = patientService.findByEmail(auth.getName());
        return ResponseEntity.ok(new PatientPersonalInfoDto(
                p.getLastName(), p.getFirstName(), p.getMiddleName(),
                p.getPassportSeries(), p.getPassportNumber(),
                p.getPassportIssueDate(), p.getPassportIssuedBy(),
                p.getIdentificationNumber()
        ));
    }

    @PostMapping("/personal-info")
    public ResponseEntity<Map<String,String>> saveInfo(
            @RequestBody PatientPersonalInfoDto dto,
            Authentication auth) {
        patientService.updatePersonalInfo(auth.getName(), dto);
        return ResponseEntity.ok(Map.of("status","ok"));
    }
}
