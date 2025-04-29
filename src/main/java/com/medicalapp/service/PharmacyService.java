package com.medicalapp.service;

import com.medicalapp.model.Pharmacy;
import com.medicalapp.repository.PharmacyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PharmacyService {

    private final PharmacyRepository repo;

    public PharmacyService(PharmacyRepository repo) {
        this.repo = repo;
    }

    // Все аптеки
    public List<Pharmacy> getAllPharmacies() {
        return repo.findAll();
    }

    // Аптека по ID
    public Pharmacy getPharmacyById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pharmacy not found: " + id));
    }

    // Создать аптеку
    public Pharmacy createPharmacy(Pharmacy p) {
        return repo.save(p);
    }

    // Обновить аптеку
    public Pharmacy updatePharmacy(Long id, Pharmacy data) {
        Pharmacy existing = getPharmacyById(id);
        existing.setCompanyName(data.getCompanyName());
        // … копируем дополнительные поля …
        return repo.save(existing);
    }

    // Удалить аптеку
    public void deletePharmacy(Long id) {
        repo.deleteById(id);
    }
}