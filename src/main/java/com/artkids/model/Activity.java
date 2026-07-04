package com.artkids.model;

import com.artkids.enums.ActivityStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Activity {
    private int id;
    private String titre;
    private String description;
    private String image;
    private LocalDate dateActivite;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private int capaciteMax;
    private int ageMin;
    private int ageMax;
    private BigDecimal prix;
    private ActivityStatus statut;
    private String lieu;
    private int categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Activity() {
    }

    public Activity(int id, String titre, String description, String image, LocalDate dateActivite,
                    LocalTime heureDebut, LocalTime heureFin, int capaciteMax, int ageMin, int ageMax,
                    BigDecimal prix, ActivityStatus statut, String lieu, int categoryId,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.image = image;
        this.dateActivite = dateActivite;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.capaciteMax = capaciteMax;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.prix = prix;
        this.statut = statut;
        this.lieu = lieu;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getDateActivite() {
        return dateActivite;
    }

    public void setDateActivite(LocalDate dateActivite) {
        this.dateActivite = dateActivite;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public int getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    public int getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(int ageMax) {
        this.ageMax = ageMax;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public ActivityStatus getStatut() {
        return statut;
    }

    public void setStatut(ActivityStatus statut) {
        this.statut = statut;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public boolean isFuture() {
        return dateActivite != null && dateActivite.isAfter(LocalDate.now());
    }

    public boolean isOpen() {
        return statut == ActivityStatus.OUVERTE;
    }

    public boolean isCancelled() {
        return statut == ActivityStatus.ANNULEE;
    }

    public boolean isComplete() {
        return statut == ActivityStatus.COMPLETE;
    }
}
