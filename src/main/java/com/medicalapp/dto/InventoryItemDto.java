// src/main/java/com/medicalapp/dto/InventoryItemDto.java
package com.medicalapp.dto;

import java.time.LocalDate;

public class InventoryItemDto {
    private String name;
    private String country;
    private String volume;
    private Integer quantity;
    private LocalDate expiryDate;

    // --- конструктор без-аргументов (нужен Jackson) ---
    public InventoryItemDto() {}

    // --- геттеры/сеттеры ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getVolume() { return volume; }
    public void setVolume(String volume) { this.volume = volume; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}
