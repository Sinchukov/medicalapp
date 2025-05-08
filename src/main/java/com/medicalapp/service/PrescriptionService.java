// src/main/java/com/medicalapp/service/PrescriptionService.java
package com.medicalapp.service;

import com.medicalapp.model.Prescription;

import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    // Получить все рецепты, выписанные данным врачом
    List<Prescription> getByDoctor(String doctorEmail);

    // Получить все рецепты данного пациента
    List<Prescription> getByPatient(String patientEmail);

    // Создать новый рецепт
    Prescription create(Prescription prescription);

    // Найти рецепт по ID
    Optional<Prescription> findById(Long id);

    /**
     * Пометить рецепт как выданный и уменьшить на 1 количество на складе.
     * @param prescriptionId ID рецепта
     * @return true, если рецепт успешно выдан и на складе было достаточно товара;
     *         false, если рецепт не найден, уже выдан или нет товара на складе.
     */
    boolean dispenseAndReduceStock(Long prescriptionId);
}
