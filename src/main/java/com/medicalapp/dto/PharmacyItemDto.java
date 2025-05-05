package com.medicalapp.dto;

public class PharmacyItemDto {
    private Long id;
    private String name;
    private String country;
    private Integer quantity;
    private String volume;      // ← здесь volume, а не dosage
    private String expiryDate;  // yyyy-MM-dd

    // ... конструкторы ...

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
