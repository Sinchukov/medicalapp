// src/main/java/com/medicalapp/model/InventoryItem.java
package com.medicalapp.model;

import javax.persistence.*;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Название лекарства
    @Column(nullable = false)
    private String name;

    // Страна-производитель
    @Column(nullable = false)
    private String country;

    // Объём / дозировка
    @Column(nullable = false)
    private String volume;

    // Количество на складе
    @Column(nullable = false)
    private Integer quantity;

    // Дата истечения срока
    @Column(name = "expiry_date", nullable = false)
    private java.time.LocalDate expiryDate;

    // Связь с аптекой
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    // --- Геттеры / Сеттеры ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getVolume() { return volume; }
    public void setVolume(String volume) { this.volume = volume; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public java.time.LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(java.time.LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public Pharmacy getPharmacy() { return pharmacy; }
    public void setPharmacy(Pharmacy pharmacy) { this.pharmacy = pharmacy; }
}
