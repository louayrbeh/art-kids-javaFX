package com.artkids.model;

import com.artkids.enums.Sexe;
import com.artkids.util.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Child {
    private int id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private Sexe sexe;
    private int parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Child() {
    }

    public Child(int id, String nom, String prenom, LocalDate dateNaissance, Sexe sexe, int parentId,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.parentId = parentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getAge() {
        return DateUtils.calculateAge(dateNaissance);
    }

    public String getFullName() {
        return (prenom == null ? "" : prenom) + " " + (nom == null ? "" : nom);
    }
}
