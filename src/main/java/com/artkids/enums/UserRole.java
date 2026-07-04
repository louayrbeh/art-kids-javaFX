package com.artkids.enums;

public enum UserRole {
    ADMIN("Administrateur"),
    PARENT("Parent");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
