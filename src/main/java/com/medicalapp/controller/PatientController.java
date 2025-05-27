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

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientService      patientService;
    private final PrescriptionService prescriptionService;
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public PatientController(PatientService patientService,
                             PrescriptionService prescriptionService) {
        this.patientService      = patientService;
        this.prescriptionService = prescriptionService;
    }

    // 1) Профиль пациента
    @GetMapping("/profile")
    public ResponseEntity<PatientProfileDto> profile(Authentication auth) {
        Patient p = patientService.findByEmail(auth.getName());
        // Здесь важно: первым аргументом передаём роль (TYPE),
        // вторым — email, третьим — дату регистрации
        PatientProfileDto dto = new PatientProfileDto(
                p.getRole().name(),              // type
                p.getEmail(),                    // email
                p.getRegistrationDate().format(DMY) // registered
        );
        return ResponseEntity.ok(dto);
    }

    // 2) Личные данные пациента (паспорт и т.п.)
    @GetMapping("/personal-info")
    public PatientPersonalInfoDto getInfo(Authentication auth) {
        Patient p = patientService.findByEmail(auth.getName());
        return new PatientPersonalInfoDto(
                p.getLastName(),
                p.getFirstName(),
                p.getMiddleName(),
                p.getPassportSeriesAndNumber(),
                p.getPassportIssueDateStr(),
                p.getPassportIssuedBy(),
                p.getIdentificationNumber()
        );
    }

    // 3) Сохранить личные данные
    @PostMapping("/personal-info")
    public Map<String,String> saveInfo(
            @RequestBody PatientPersonalInfoDto dto,
            Authentication auth
    ) {
        patientService.updatePersonalInfo(
                auth.getName(),
                dto.getLastName(),
                dto.getFirstName(),
                dto.getMiddleName(),
                dto.getPassportSeriesAndNumber(),
                dto.getPassportIssueDate(),
                dto.getPassportIssuedBy(),
                dto.getIdentificationNumber()
        );
        return Map.of("status","ok");
    }

    // 4) Список собственных рецептов
    private static final DateTimeFormatter DMY_HMS =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @GetMapping("/recipes")
    public ResponseEntity<List<Map<String,String>>> recipes(Authentication auth) {
        List<Prescription> all = prescriptionService.getByPatient(auth.getName());
        List<Map<String,String>> out = all.stream()
                .filter(p -> "ISSUED".equalsIgnoreCase(p.getStatus()))
                .map(r -> Map.of(
                        "id",      r.getId().toString(),
                        "name",    r.getDrugName(),
                        "dosage",  r.getDosage(),
                        "issued",  r.getIssueDateTime().format(DMY_HMS),   // <-- здесь дата+время
                        "expiry",  r.getExpiryDate().format(DMY)
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }
}
