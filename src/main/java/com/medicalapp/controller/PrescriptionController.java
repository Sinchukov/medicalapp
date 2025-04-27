package com.medicalapp.controller;

import com.medicalapp.model.Prescription;
import com.medicalapp.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService presService;

    @Autowired
    public PrescriptionController(PrescriptionService presService) {
        this.presService = presService;
    }

    @GetMapping("/patient/{email}")
    public List<Prescription> getPrescriptionsByPatient(@PathVariable String email) {
        return presService.byPatient(email);
    }

    @GetMapping("/doctor/{email}")
    public List<Prescription> getPrescriptionsByDoctor(@PathVariable String email) {
        return presService.byDoctor(email);
    }
}
