package com.medicalapp.controller;

import com.medicalapp.dto.CheckPatientDto;
import com.medicalapp.dto.CreatePrescriptionDto;
import com.medicalapp.dto.DoctorPersonalInfoDto;
import com.medicalapp.dto.DoctorProfileDto;
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
        String registered = d.getRegistrationDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return ResponseEntity.ok(
                new DoctorProfileDto(d.getEmail(), registered, d.getRole().name())
        );
    }

    @GetMapping("/personal-info")
    public ResponseEntity<DoctorPersonalInfoDto> getInfo(Authentication auth) {
        Doctor d = doctorService.findByEmail(auth.getName());
        return ResponseEntity.ok(
                new DoctorPersonalInfoDto(
                        d.getLastName(), d.getFirstName(), d.getMiddleName(),
                        d.getPassportSeries(), d.getPassportNumber(),
                        d.getPassportIssueDate(), d.getPassportIssuedBy(),
                        d.getIdentificationNumber(),
                        d.getQualification(), d.getExperience(), d.getWorkplace()
                )
        );
    }

    @PostMapping("/personal-info")
    public ResponseEntity<Map<String,String>> saveInfo(
            @RequestBody DoctorPersonalInfoDto dto,
            Authentication auth) {
        doctorService.updatePersonalInfo(auth.getName(), dto);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }


    @GetMapping("/prescriptions")
    public ResponseEntity<List<Map<String,String>>> prescriptions(Authentication auth) {
        List<Prescription> list = prescriptionService.getByDoctor(auth.getName());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<Map<String,String>> dto = list.stream()
                .map(r -> Map.of(
                        "patient",  r.getPatientEmail(),
                        "date",     r.getDateIssued().format(fmt),
                        "medicine", r.getDrugName()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    // → Шаг проверки пациента: теперь с логированием приходящих данных
    @PostMapping("/check-patient")
    public ResponseEntity<?> checkPatient(@RequestBody CheckPatientDto dto) {
        log.info("Doctor '{}' checking patient with data: {}",
                /*current doctor*/ "",
                dto);
        Patient p = patientService.findByPersonalData(dto);
        if (p == null) {
            log.warn("Patient not found for {}", dto);
            return ResponseEntity.status(404)
                    .body(Map.of("error", "Patient not found or data mismatch"));
        }
        log.info("Patient found: {}", p.getEmail());
        return ResponseEntity.ok(Map.of(
                "status", "found",
                "email", p.getEmail(),
                "firstName", p.getFirstName(),
                "lastName", p.getLastName()
        ));
    }

    @PostMapping("/prescriptions")
    public ResponseEntity<?> createPrescription(@RequestBody CreatePrescriptionDto dto,
                                                Authentication auth) {
        Prescription r = new Prescription();
        r.setPatientEmail(dto.getPatientEmail());
        r.setDoctorEmail(auth.getName());
        r.setDrugName(dto.getDrugName());
        r.setDosage(dto.getDosage());
        r.setExpiry(dto.getExpiry());
        r.setDateIssued(LocalDate.now());
        prescriptionService.create(r);
        log.info("Created prescription for {} by doctor {}",
                dto.getPatientEmail(), auth.getName());
        return ResponseEntity.ok(Map.of("status", "created"));
    }
}
