// src/main/java/com/medicalapp/controller/PharmacyController.java
package com.medicalapp.controller;

import com.medicalapp.dto.CheckPatientDto;
import com.medicalapp.dto.PharmacyRecipeDto;
import com.medicalapp.dto.InventoryItemDto;
import com.medicalapp.model.InventoryItem;
import com.medicalapp.model.Patient;
import com.medicalapp.model.Pharmacy;
import com.medicalapp.service.PatientService;
import com.medicalapp.service.PrescriptionService;
import com.medicalapp.repository.InventoryItemRepository;
import com.medicalapp.repository.PharmacyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyController {
    private final PharmacyRepository   phrRepo;
    private final InventoryItemRepository invRepo;
    private final PatientService       patientService;
    private final PrescriptionService  prescriptionService;
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public PharmacyController(PharmacyRepository phrRepo,
                              InventoryItemRepository invRepo,
                              PatientService patientService,
                              PrescriptionService prescriptionService) {
        this.phrRepo            = phrRepo;
        this.invRepo            = invRepo;
        this.patientService     = patientService;
        this.prescriptionService = prescriptionService;
    }

    /** 1) Профиль аптеки **/
    @GetMapping("/profile")
    public ResponseEntity<Map<String,String>> profile(Authentication auth) {
        Pharmacy p = phrRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));
        return ResponseEntity.ok(Map.of(
                "type",        p.getRole().name(),
                "companyName", p.getCompanyName(),
                "email",       p.getEmail(),
                "registered",  p.getRegistrationDate().format(DMY)
        ));
    }

    /** 2) Список товаров аптеки **/
    @GetMapping("/items")
    public ResponseEntity<List<InventoryItem>> getItems(Authentication auth) {
        Pharmacy p = phrRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));
        return ResponseEntity.ok(
                invRepo.findAllByPharmacyId(p.getId())
        );
    }

    /** 3) Добавить товар **/
    @PostMapping("/items")
    public ResponseEntity<Map<String,String>> addItem(
            Authentication auth,
            @RequestBody InventoryItemDto dto
    ) {
        Pharmacy p = phrRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));

        InventoryItem item = new InventoryItem();
        item.setName(dto.getName());
        item.setCountry(dto.getCountry());
        item.setVolume(dto.getVolume());
        item.setQuantity(dto.getQuantity());
        item.setExpiryDate(dto.getExpiryDate());
        item.setPharmacy(p);

        invRepo.save(item);
        return ResponseEntity.ok(Map.of("status","created"));
    }

    /** 4) Поиск пациента и выдача его рецептов **/
    @PostMapping("/patients/check")
    public ResponseEntity<?> checkPatient(@RequestBody CheckPatientDto dto) {
        Patient p = patientService.findByPersonalData(
                dto.getLastName(), dto.getFirstName(), dto.getMiddleName(),
                dto.getPassportSeriesAndNumber(),
                dto.getPassportIssueDate(), dto.getPassportIssuedBy(),
                dto.getIdentificationNumber()
        );
        if (p == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("error","Patient Not Found"));
        }

        List<PharmacyRecipeDto> recipes = prescriptionService
                .getByPatient(p.getEmail())
                .stream()
                .map(pr -> {
                    PharmacyRecipeDto out = new PharmacyRecipeDto();
                    out.setPrescriptionId(pr.getId());
                    out.setDateIssued(pr.getIssueDate().format(DMY));
                    out.setExpiryDate(pr.getExpiryDate().format(DMY));
                    out.setMedicine(pr.getDrugName());
                    out.setDosage(pr.getDosage());
                    out.setDoctorEmail(pr.getDoctorEmail());
                    out.setDispensed(pr.isDispensed());
                    return out;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(recipes);
    }
}
