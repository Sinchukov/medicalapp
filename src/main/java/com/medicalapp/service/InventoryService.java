package com.medicalapp.service;

public interface InventoryService {
    boolean reduceStock(String name, String dosage, int qty);
}
