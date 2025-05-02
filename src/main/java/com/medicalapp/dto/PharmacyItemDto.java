// src/main/java/com/medicalapp/dto/PharmacyItemDto.java
package com.medicalapp.dto;

import java.time.LocalDate;

/**
 * DTO для передачи на фронт и приёма из фронта.
 */
public class PharmacyItemDto {
    private Long id;
    private String name;
    private String country;
    private Integer quantity;
    private String volume;
    private String expiryDate; // строка yyyy-MM-dd

    public PharmacyItemDto() {}

    public PharmacyItemDto(Long id,
                           String name,
                           String country,
                           Integer quantity,
                           String volume,
                           String expiryDate) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.quantity = quantity;
        this.volume = volume;
        this.expiryDate = expiryDate;
    }

    // --- геттеры/сеттеры ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getVolume() { return volume; }
    public void setVolume(String volume) { this.volume = volume; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}
