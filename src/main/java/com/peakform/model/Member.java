package com.peakform.model;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private Integer age;
    private String suburb;
    private String email;

    private String medicalNote;

    private String availableSlot;

    public Member() {}

    public Member(String fullName, Integer age, String suburb, String email, String medicalNote, String availableSlot) {
        this.fullName = fullName;
        this.age = age;
        this.suburb = suburb;
        this.email = email;
        this.medicalNote = medicalNote;
        this.availableSlot = availableSlot;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getSuburb() { return suburb; }
    public void setSuburb(String suburb) { this.suburb = suburb; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMedicalNote() { return medicalNote; }
    public void setMedicalNote(String medicalNote) { this.medicalNote = medicalNote; }
    public String getAvailableSlot() { return availableSlot; }
    public void setAvailableSlot(String availableSlot) { this.availableSlot = availableSlot; }
}
