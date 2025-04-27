package com.medicalapp.controller;

import com.medicalapp.dto.DoctorPersonalInfoDto;
import com.medicalapp.dto.DoctorProfileDto;
import com.medicalapp.model.Doctor;
import com.medicalapp.model.Prescription;
import com.medicalapp.service.DoctorService;
import com.medicalapp.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;

    public DoctorController(DoctorService doctorService,
                            PrescriptionService prescriptionService) {
        this.doctorService       = doctorService;
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
}
