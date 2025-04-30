// src/main/java/com/medicalapp/controller/PatientController.java
package com.medicalapp.controller;

import com.medicalapp.dto.PatientPersonalInfoDto;
import com.medicalapp.dto.PatientProfileDto;
import com.medicalapp.model.Patient;
import com.medicalapp.model.Prescription;
import com.medicalapp.service.PatientService;
import com.medicalapp.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientService patientService;
    private final PrescriptionService prescriptionService;   // ← добавили
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    // Конструктор теперь принимает оба сервиса
    public PatientController(PatientService patientService,
                             PrescriptionService prescriptionService) {
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
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
                p.getPassportIssueDateStr(),
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
                dto.getPassportIssueDate(),
                dto.getPassportIssuedBy(), dto.getIdentificationNumber()
        );
        return Map.of("status","ok");
    }

    // ← НОВЫЙ метод для выдачи списка рецептов пациенту
    @GetMapping("/recipes")
    public ResponseEntity<List<Map<String,String>>> recipes(Authentication auth) {
        List<Prescription> list = prescriptionService.getByPatient(auth.getName());
        var fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        var dto = list.stream()
                .map(r -> Map.of(
                        "name",   r.getDrugName(),
                        "dosage", r.getDosage(),
                        "expiry", r.getExpiryDate().format(fmt)
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
}
