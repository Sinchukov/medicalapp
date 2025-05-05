package com.medicalapp.controller;

import com.medicalapp.dto.*;
import com.medicalapp.model.*;
import com.medicalapp.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final DoctorService       doctorService;
    private final PatientService      patientService;
    private final PrescriptionService prescriptionService;

    public DoctorController(DoctorService ds, PatientService ps, PrescriptionService prs) {
        this.doctorService = ds;
        this.patientService = ps;
        this.prescriptionService = prs;
    }

    @GetMapping("/profile")
    public ResponseEntity<DoctorProfileDto> profile(Authentication auth) {
        Doctor d = doctorService.findByEmail(auth.getName());
        return ResponseEntity.ok(new DoctorProfileDto(
                d.getEmail(),
                d.getRegistrationDate().format(DMY),
                d.getRole().name()
        ));
    }

    @GetMapping("/prescriptions")
    public List<DoctorPrescriptionDto> prescriptions(Authentication auth) {
        return prescriptionService.getByDoctor(auth.getName())
                .stream()
                .map(r -> {
                    Patient p = patientService.findByEmail(r.getPatientEmail());

                    DoctorPrescriptionDto.PatientInfo pi = new DoctorPrescriptionDto.PatientInfo();
                    pi.email                    = p.getEmail();
                    pi.firstName                = p.getFirstName();
                    pi.lastName                 = p.getLastName();
                    pi.middleName               = p.getMiddleName();
                    pi.passportSeriesAndNumber  = p.getPassportSeriesAndNumber();

                    DoctorPrescriptionDto.PrescriptionInfo pr = new DoctorPrescriptionDto.PrescriptionInfo();
                    pr.dateIssued = r.getIssueDate().format(DMY);
                    pr.expiryDate = r.getExpiryDate().format(DMY);
                    pr.medicine   = r.getDrugName();
                    pr.dosage     = r.getDosage();

                    DoctorPrescriptionDto dp = new DoctorPrescriptionDto();
                    dp.patient      = pi;
                    dp.prescription = pr;
                    return dp;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/check-patient")
    public ResponseEntity<?> checkPatient(@RequestBody CheckPatientDto dto) {
        Patient p = patientService.findByPersonalData(
                dto.getLastName(), dto.getFirstName(), dto.getMiddleName(),
                dto.getPassportSeriesAndNumber(),
                dto.getPassportIssueDate(), dto.getPassportIssuedBy(),
                dto.getIdentificationNumber()
        );
        if (p == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "Patient not found"));
        }
        return ResponseEntity.ok(Map.of(
                "status",    "found",
                "email",     p.getEmail(),
                "firstName", p.getFirstName(),
                "lastName",  p.getLastName()
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
        r.setIssueDate(LocalDate.now());
        r.setExpiryDate(dto.getExpiryDate());

        prescriptionService.create(r);
        return ResponseEntity.ok(Map.of("status","created"));
    }
}
