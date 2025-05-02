package com.medicalapp.controller;

import com.medicalapp.dto.CheckPatientDto;
import com.medicalapp.dto.DispenseRequestDto;
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
        this.patientService      = patientService;
        this.prescriptionService = prescriptionService;
    }

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

        List<Prescription> rxList = prescriptionService.getByPatient(p.getEmail());
        List<PharmacyRecipeDto> result = rxList.stream().map(r -> {
            PharmacyRecipeDto o = new PharmacyRecipeDto();
            o.setPrescriptionId(r.getId());
            o.setDateIssued(r.getDateIssued().format(DMY));
            o.setExpiryDate(r.getExpiryDate().format(DMY));
            o.setMedicine(r.getDrugName());
            o.setDosage(r.getDosage());
            o.setDoctorEmail(r.getDoctorEmail());
            return o;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/dispense")
    public ResponseEntity<?> dispense(
            @RequestBody DispenseRequestDto req
    ) {
        boolean ok = prescriptionService.dispense(
                req.getPatientEmail(),
                req.getDrugName(),
                req.getDosage()
        );
        if (!ok) {
            return ResponseEntity.status(400)
                    .body(Map.of("error", "Dispense failed"));
        }
        return ResponseEntity.ok(Map.of("status", "dispensed"));
    }
}
