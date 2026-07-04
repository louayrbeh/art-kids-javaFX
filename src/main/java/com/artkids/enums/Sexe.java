package com.artkids.enums;

public enum Sexe {
    GARCON("Garcon"),
    FILLE("Fille");

    private final String label;

    Sexe(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
