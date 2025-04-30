// src/main/java/com/medicalapp/controller/DoctorController.java
package com.medicalapp.controller;

import com.medicalapp.dto.DoctorProfileDto;
import com.medicalapp.dto.CheckPatientDto;
import com.medicalapp.dto.CreatePrescriptionDto;
import com.medicalapp.model.Patient;
import com.medicalapp.model.Prescription;
import com.medicalapp.model.Doctor;
import com.medicalapp.service.DoctorService;
import com.medicalapp.service.PatientService;
import com.medicalapp.service.PrescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    private static final Logger log = LoggerFactory.getLogger(DoctorController.class);
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final PrescriptionService prescriptionService;

    public DoctorController(DoctorService doctorService,
                            PatientService patientService,
                            PrescriptionService prescriptionService) {
        this.doctorService       = doctorService;
        this.patientService      = patientService;
        this.prescriptionService = prescriptionService;
    }

    /** ← НОВЫЙ эндпоинт для отдачи профиля врача */
    @GetMapping("/profile")
    public ResponseEntity<DoctorProfileDto> profile(Authentication auth) {
        Doctor d = doctorService.findByEmail(auth.getName());
        String registered = d.getRegistrationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return ResponseEntity.ok(new DoctorProfileDto(
                d.getEmail(),
                registered,
                d.getRole().name()
        ));
    }

    @GetMapping("/prescriptions")
    public ResponseEntity<List<Map<String,String>>> prescriptions(Authentication auth) {
        List<Prescription> list = prescriptionService.getByDoctor(auth.getName());
        var fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        var dto = list.stream()
                .map(r -> Map.of(
                        "patient",  r.getPatientEmail(),
                        "date",     r.getDateIssued().format(fmt),
                        "medicine", r.getDrugName()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/check-patient")
    public ResponseEntity<?> checkPatient(@RequestBody CheckPatientDto dto) {
        // не парсим здесь — передаём строку дальше:
        Patient p = patientService.findByPersonalData(
                dto.getLastName(),
                dto.getFirstName(),
                dto.getMiddleName(),
                dto.getPassportSeries(),
                dto.getPassportNumber(),
                dto.getPassportIssueDate(),      // <- String dd.MM.yyyy
                dto.getPassportIssuedBy(),
                dto.getIdentificationNumber()
        );
        if (p == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("error","Patient not found or data mismatch"));
        }
        return ResponseEntity.ok(Map.of(
                "status","found",
                "email", p.getEmail(),
                "firstName", p.getFirstName(),
                "lastName", p.getLastName()
        ));
    }

    // src/main/java/com/medicalapp/controller/DoctorController.java
    @PostMapping("/prescriptions")
    public ResponseEntity<?> createPrescription(
            @RequestBody CreatePrescriptionDto dto,
            Authentication auth
    ) {
        Prescription r = new Prescription();
        r.setPatientEmail(dto.getPatientEmail());
        r.setDoctorEmail(auth.getName());
        r.setDrugName(dto.getDrugName());
        r.setDosage(dto.getDosage());

        LocalDate today = LocalDate.now();
        r.setDateIssued(today);            // → prescription_issue_date
        r.setExpiryDate(dto.getExpiryDate()); // → prescription_expiry_date

        prescriptionService.create(r);
        return ResponseEntity.ok(Map.of("status","created"));
    }

}
