package com.artkids.enums;

public enum ActivityStatus {
    OUVERTE("Ouverte"),
    COMPLETE("Complete"),
    ANNULEE("Annulee"),
    TERMINEE("Terminee");

    private final String label;

    ActivityStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
