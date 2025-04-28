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
    private final PrescriptionService prescriptionService;

    public PatientController(PatientService patientService,
                             PrescriptionService prescriptionService) {
        this.patientService      = patientService;
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/profile")
    public ResponseEntity<PatientProfileDto> profile(Authentication auth) throws Throwable {
        Patient p = patientService.findByEmail(auth.getName());
        String registered = p.getRegistrationDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return ResponseEntity.ok(
                new PatientProfileDto(p.getEmail(), registered, p.getRole().name())
        );
    }

    @GetMapping("/personal-info")
    public ResponseEntity<PatientPersonalInfoDto> getInfo(Authentication auth) throws Throwable {
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
            Authentication auth) throws Throwable {
        patientService.updatePersonalInfo(auth.getName(), dto);
        return ResponseEntity.ok(Map.of("status","ok"));
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<Map<String,String>>> recipes(Authentication auth) {
        List<Prescription> list = prescriptionService.getByPatient(auth.getName());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<Map<String,String>> dto = list.stream()
                .map(r -> Map.of(
                        "name",   r.getDrugName(),
                        "dosage", r.getDosage(),
                        "expiry", r.getExpiry().format(fmt)
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
}
