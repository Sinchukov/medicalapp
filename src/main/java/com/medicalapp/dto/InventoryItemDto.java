package com.medicalapp.dto;

import java.time.LocalDate;

public class InventoryItemDto {
    private String name;
    private String country;
    private Integer quantity;
    private String volume;

    // ← НОВОЕ поле
    private LocalDate expiryDate;
    // getters / setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getVolume() { return volume; }
    public void setVolume(String volume) { this.volume = volume; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}