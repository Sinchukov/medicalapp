// src/main/java/com/medicalapp/service/InventoryService.java
package com.medicalapp.service;

public interface InventoryService {
    /**
     * Списать count единиц товара name+dosage.
     * @return true — если товар есть и списан, иначе false.
     */
    boolean reduceStock(String name, String dosage, int count);
}
