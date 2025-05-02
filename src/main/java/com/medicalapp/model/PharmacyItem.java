// src/main/java/com/medicalapp/model/PharmacyItem.java
package com.medicalapp.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pharmacy_items")
public class PharmacyItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // например, уникальное наименование
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private String volume;      // "10mg", "0.5l"...
    @Column(nullable = false)
    private LocalDate expiryDate;

    // геттеры/сеттеры

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

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}
