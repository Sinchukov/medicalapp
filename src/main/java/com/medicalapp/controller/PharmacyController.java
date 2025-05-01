// src/main/java/com/medicalapp/controller/PharmacyController.java
package com.medicalapp.controller;

import com.medicalapp.dto.InventoryItemDto;
import com.medicalapp.dto.PharmacyRecipeDto;
import com.medicalapp.dto.CheckPatientDto;
import com.medicalapp.model.InventoryItem;
import com.medicalapp.model.Patient;
import com.medicalapp.model.Pharmacy;
import com.medicalapp.model.Prescription;
import com.medicalapp.repository.InventoryItemRepository;
import com.medicalapp.repository.PharmacyRepository;
import com.medicalapp.service.PatientService;
import com.medicalapp.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pharmacy")
public class  PharmacyController {

    private final PharmacyRepository phrRepo;
    private final InventoryItemRepository invRepo;
    private final PatientService patientService;
    private final PrescriptionService prescriptionService;

    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public PharmacyController(
            PharmacyRepository phrRepo,
            InventoryItemRepository invRepo,
            PatientService patientService,
            PrescriptionService prescriptionService
    ) {
        this.phrRepo = phrRepo;
        this.invRepo = invRepo;
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
    }

    /**
     * Профиль аптеки
     */
    @GetMapping("/profile")
    public ResponseEntity<?> profile(Authentication auth) {
        Pharmacy p = phrRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));
        return ResponseEntity.ok(Map.of(
                "type",        p.getRole().name(),
                "companyName", p.getCompanyName(),
                "email",       p.getEmail(),
                "registered",  p.getRegistrationDate().format(DMY)
        ));
    }

    /**
     * Список товаров аптеки
     */
    @GetMapping("/items")
    public ResponseEntity<List<InventoryItem>> getItems(Authentication auth) {
        Pharmacy p = phrRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));
        return ResponseEntity.ok(invRepo.findAllByPharmacyId(p.getId()));
    }

    /**
     * Добавление нового товара
     */
    @PostMapping("/items")
    public ResponseEntity<?> addItem(
            Authentication auth,
            @RequestBody InventoryItemDto dto
    ) {
        Pharmacy p = phrRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));

        InventoryItem item = new InventoryItem();
        item.setName(dto.getName());
        item.setCountry(dto.getCountry());
        item.setQuantity(dto.getQuantity());
        item.setVolume(dto.getVolume());
        item.setExpiryDate(dto.getExpiryDate());  // assuming dto has expiryDate field of type LocalDate or String
        item.setPharmacy(p);

        invRepo.save(item);
        return ResponseEntity.ok(Map.of("status", "created"));
    }

    /**
     * Поиск пациента по паспортным данным и возвращение его рецептов
     */
    @PostMapping("/check-patient")
    public ResponseEntity<?> checkPatient(
            @RequestBody CheckPatientDto dto
    ) {
        // 1) Найти пациента по данным
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

        // 2) Достать все его рецепты
        List<Prescription> rxList = prescriptionService.getByPatient(p.getEmail());

        // 3) Преобразовать в DTO
        List<PharmacyRecipeDto> result = rxList.stream()
                .map(r -> {
                    PharmacyRecipeDto out = new PharmacyRecipeDto();
                    out.setDateIssued(r.getDateIssued().format(DMY));
                    out.setExpiryDate(r.getExpiryDate().format(DMY));
                    out.setMedicine(r.getDrugName());
                    out.setDosage(r.getDosage());
                    out.setDoctorEmail(r.getDoctorEmail());
                    return out;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
