package com.medicalapp.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pharmacies")
public class Pharmacy extends UserBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    @Column(nullable=false)
    private LocalDate registrationDate;

    @Column(nullable=false)
    private String companyName;  // вместо fullName

    public Pharmacy() { }

    public Pharmacy(Long id, String email, String password, Role role,
                    LocalDate registrationDate, String companyName) {
        this.id = id; this.email = email; this.password = password;
        this.role = role; this.registrationDate = registrationDate;
        this.companyName = companyName;
    }

    @Override public Long getId() { return id; }
    @Override public String getEmail() { return email; }
    @Override public String getPassword() { return password; }
    @Override public Role getRole() { return role; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public String getCompanyName() { return companyName; }

    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}