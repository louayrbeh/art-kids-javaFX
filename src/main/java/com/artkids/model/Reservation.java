package com.artkids.model;

import com.artkids.enums.ReservationStatus;

import java.time.LocalDateTime;

public class Reservation {
    private int id;
    private LocalDateTime dateReservation;
    private ReservationStatus statut;
    private int childId;
    private int activityId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Reservation() {
    }

    public Reservation(int id, LocalDateTime dateReservation, ReservationStatus statut,
                       int childId, int activityId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.dateReservation = dateReservation;
        this.statut = statut;
        this.childId = childId;
        this.activityId = activityId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public ReservationStatus getStatut() {
        return statut;
    }

    public void setStatut(ReservationStatus statut) {
        this.statut = statut;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
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

    public boolean isEnAttente() {
        return statut == ReservationStatus.EN_ATTENTE;
    }

    public boolean isConfirmee() {
        return statut == ReservationStatus.CONFIRMEE;
    }

    public boolean isAnnulee() {
        return statut == ReservationStatus.ANNULEE;
    }
}
