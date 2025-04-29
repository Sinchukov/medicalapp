// src/main/java/com/medicalapp/controller/PatientController.java
package com.medicalapp.controller;

import com.medicalapp.dto.PatientPersonalInfoDto;
import com.medicalapp.dto.PatientProfileDto;
import com.medicalapp.model.Patient;
import com.medicalapp.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;          // <- SPRING import!
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientService patientService;
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/profile")
    public ResponseEntity<PatientProfileDto> profile(Authentication auth) {
        Patient p = patientService.findByEmail(auth.getName());
        String registered = p.getRegistrationDate().format(DMY);
        return ResponseEntity.ok(new PatientProfileDto(
                p.getEmail(), p.getRole().name(), registered
        ));
    }

    @GetMapping("/personal-info")
    public PatientPersonalInfoDto getInfo(Authentication auth) {
        Patient p = patientService.findByEmail(auth.getName());
        return new PatientPersonalInfoDto(
                p.getLastName(), p.getFirstName(), p.getMiddleName(),
                p.getPassportSeries(), p.getPassportNumber(),
                p.getPassportIssueDateStr(),             // <- строка
                p.getPassportIssuedBy(), p.getIdentificationNumber()
        );
    }

    @PostMapping("/personal-info")
    public Map<String,String> saveInfo(
            @RequestBody PatientPersonalInfoDto dto,
            Authentication auth
    ) {
        patientService.updatePersonalInfo(
                auth.getName(),
                dto.getLastName(), dto.getFirstName(), dto.getMiddleName(),
                dto.getPassportSeries(), dto.getPassportNumber(),
                dto.getPassportIssueDate(),  // строка
                dto.getPassportIssuedBy(), dto.getIdentificationNumber()
        );
        return Map.of("status","ok");
    }
}
