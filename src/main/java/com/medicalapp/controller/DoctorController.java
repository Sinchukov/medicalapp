// src/main/java/com/medicalapp/controller/DoctorController.java
package com.medicalapp.controller;

import com.medicalapp.dto.CheckPatientDto;
import com.medicalapp.dto.CreatePrescriptionDto;
import com.medicalapp.dto.DoctorProfileDto;
import com.medicalapp.dto.DoctorPrescriptionDto;
import com.medicalapp.model.Doctor;
import com.medicalapp.model.Patient;
import com.medicalapp.model.Prescription;
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

    @GetMapping("/profile")
    public ResponseEntity<DoctorProfileDto> profile(Authentication auth) {
        Doctor d = doctorService.findByEmail(auth.getName());
        String registered = d.getRegistrationDate().format(DMY);
        return ResponseEntity.ok(new DoctorProfileDto(
                d.getEmail(),
                registered,
                d.getRole().name()
        ));
    }

    // ← вот этот метод мы меняем:
    @GetMapping("/prescriptions")
    public ResponseEntity<List<DoctorPrescriptionDto>> prescriptions(Authentication auth) {
        List<Prescription> list = prescriptionService.getByDoctor(auth.getName());

        List<DoctorPrescriptionDto> dto = list.stream().map(r -> {
            // 1) пациент
            Patient p = patientService.findByEmail(r.getPatientEmail());
            DoctorPrescriptionDto.PatientInfo pi = new DoctorPrescriptionDto.PatientInfo();
            pi.email          = p.getEmail();
            pi.firstName      = p.getFirstName();
            pi.lastName       = p.getLastName();
            pi.middleName     = p.getMiddleName();
            pi.passportSeriesAndNumber = p.getPassportSeriesAndNumber();

            // 2) сам рецепт
            DoctorPrescriptionDto.PrescriptionInfo pr = new DoctorPrescriptionDto.PrescriptionInfo();
            pr.dateIssued = r.getDateIssued().format(DMY);
            pr.expiryDate = r.getExpiryDate().format(DMY);
            pr.medicine   = r.getDrugName();
            pr.dosage     = r.getDosage();

            // 3) собираем общий DTO
            DoctorPrescriptionDto dp = new DoctorPrescriptionDto();
            dp.patient      = pi;
            dp.prescription = pr;
            return dp;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/check-patient")
    public ResponseEntity<?> checkPatient(@RequestBody CheckPatientDto dto) {
        Patient p = patientService.findByPersonalData(
                dto.getLastName(), dto.getFirstName(), dto.getMiddleName(),
                dto.getPassportSeriesAndNumber(),     // ← единое
                dto.getPassportIssueDate(), dto.getPassportIssuedBy(),
                dto.getIdentificationNumber()
        );
        if (p == null) {
            return ResponseEntity.status(404).body(Map.of("error","Patient not found"));
        }
        return ResponseEntity.ok(Map.of(
                "status","found",
                "email", p.getEmail(),
                "firstName", p.getFirstName(),
                "lastName", p.getLastName()
        ));
    }

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
        r.setDateIssued(today);
        r.setExpiryDate(dto.getExpiryDate());

        prescriptionService.create(r);
        return ResponseEntity.ok(Map.of("status","created"));
    }
}
