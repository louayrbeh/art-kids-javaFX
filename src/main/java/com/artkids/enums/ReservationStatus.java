package com.artkids.enums;

public enum ReservationStatus {
    EN_ATTENTE("En attente"),
    CONFIRMEE("Confirmee"),
    ANNULEE("Annulee"),
    TERMINEE("Terminee");

    private final String label;

    ReservationStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
