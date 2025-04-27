package com.medicalapp.model;

import javax.persistence.*;

@Entity
@Table(name="inventory_items")
public class InventoryItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(nullable=false)
    private Pharmacy pharmacy;

    private String name, country, quantity, volume;

    public Long getId(){return id;}
    public Pharmacy getPharmacy(){return pharmacy;}
    public void setPharmacy(Pharmacy p){this.pharmacy=p;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getCountry(){return country;} public void setCountry(String v){country=v;}
    public String getQuantity(){return quantity;} public void setQuantity(String v){quantity=v;}
    public String getVolume(){return volume;} public void setVolume(String v){volume=v;}
}
