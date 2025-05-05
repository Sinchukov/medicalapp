// src/main/java/com/medicalapp/controller/PharmacyPatientController.java
package com.medicalapp.controller;

import com.medicalapp.dto.CheckPatientDto;
import com.medicalapp.dto.PharmacyRecipeDto;
import com.medicalapp.model.Patient;
import com.medicalapp.model.Prescription;
import com.medicalapp.service.PatientService;
import com.medicalapp.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pharmacy/patients")
public class PharmacyPatientController {

    private final PatientService      patientService;
    private final PrescriptionService prescriptionService;
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public PharmacyPatientController(
            PatientService patientService,
            PrescriptionService prescriptionService
    ) {
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
    }

    // теперь без @PreAuthorize — доступно всем валидным токенам
    @PostMapping("/check")
    public ResponseEntity<?> checkPatient(@RequestBody CheckPatientDto dto) {
        Patient p = patientService.findByPersonalData(
                dto.getLastName(), dto.getFirstName(), dto.getMiddleName(),
                dto.getPassportSeriesAndNumber(),
                dto.getPassportIssueDate(), dto.getPassportIssuedBy(),
                dto.getIdentificationNumber()
        );
        if (p == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "Patient Not Found"));
        }

        List<PharmacyRecipeDto> list = prescriptionService
                .getByPatient(p.getEmail())
                .stream()
                .map(pr -> {
                    PharmacyRecipeDto out = new PharmacyRecipeDto();
                    out.setDateIssued(pr.getIssueDate().format(DMY));
                    out.setExpiryDate(pr.getExpiryDate().format(DMY));
                    out.setDrugName(pr.getDrugName());
                    out.setDosage(pr.getDosage());
                    out.setDoctorEmail(pr.getDoctorEmail());
                    out.setStatus(pr.getStatus());
                    out.setPrescriptionId(pr.getId());
                    return out;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }
}
