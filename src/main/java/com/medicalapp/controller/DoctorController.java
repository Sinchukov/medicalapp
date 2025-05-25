package com.medicalapp.controller;

import com.medicalapp.dto.CheckPatientDto;
import com.medicalapp.dto.CreatePrescriptionDto;
import com.medicalapp.dto.DoctorPrescriptionDto;
import com.medicalapp.model.Doctor;
import com.medicalapp.model.Patient;
import com.medicalapp.model.Prescription;
import com.medicalapp.service.DoctorService;
import com.medicalapp.service.PatientService;
import com.medicalapp.service.PrescriptionService;
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
    public ResponseEntity<Map<String,String>> profile(Authentication auth) {
        Doctor d = doctorService.findByEmail(auth.getName());
        // Обратите внимание: возвращаем ключи type/email/registered
        return ResponseEntity.ok(Map.of(
                "type",       d.getRole().name(),
                "email",      d.getEmail(),
                "registered", d.getRegistrationDate().format(DMY)
        ));
    }

    @GetMapping("/prescriptions")
    public List<DoctorPrescriptionDto> prescriptions(Authentication auth) {
        return prescriptionService.getByDoctor(auth.getName()).stream()
                .map(r -> {
                    // Инфа о пациенте
                    Patient p = patientService.findByEmail(r.getPatientEmail());
                    DoctorPrescriptionDto.PatientInfo pi = new DoctorPrescriptionDto.PatientInfo();
                    pi.email  = p.getEmail();
                    pi.firstName  = p.getFirstName();
                    pi.lastName   = p.getLastName();
                    pi.middleName = p.getMiddleName();
                    pi.passportSeriesAndNumber = p.getPassportSeriesAndNumber();

                    // Инфа о рецепте
                    DoctorPrescriptionDto.PrescriptionInfo pr = new DoctorPrescriptionDto.PrescriptionInfo();
                    pr.dateIssued = r.getIssueDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    pr.expiryDate = r.getExpiryDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    pr.medicine   = r.getDrugName();
                    pr.dosage     = r.getDosage();
                    pr.status     = r.getStatus();
                    pr.dispensed  = r.isDispensed();           // <-- новый флаг

                    DoctorPrescriptionDto dto = new DoctorPrescriptionDto();
                    dto.patient = pi;
                    dto.prescription = pr;
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @PostMapping("/check-patient")
    public ResponseEntity<?> checkPatient(@RequestBody CheckPatientDto dto) {
        Patient p = patientService.findByPersonalData(
                dto.getLastName(),
                dto.getFirstName(),
                dto.getMiddleName(),
                dto.getPassportSeriesAndNumber(),
                dto.getPassportIssueDate(),
                dto.getPassportIssuedBy(),
                dto.getIdentificationNumber()
        );
        if (p == null) {
            return ResponseEntity.status(404).body(Map.of("error","Пациент не найден"));
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
        Prescription pres = new Prescription();
        pres.setPatientEmail(dto.getPatientEmail());
        pres.setDoctorEmail(auth.getName());
        pres.setDrugName(dto.getDrugName());
        pres.setDosage(dto.getDosage());
        pres.setIssueDate(LocalDate.now());
        pres.setExpiryDate(dto.getExpiryDate());
        pres.setStatus("ISSUED");

        prescriptionService.create(pres);
        return ResponseEntity.ok(Map.of("status","created"));
    }
}
