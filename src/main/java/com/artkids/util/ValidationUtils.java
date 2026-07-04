package com.artkids.util;

import java.util.regex.Pattern;

public final class ValidationUtils {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private ValidationUtils() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return !isBlank(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static void validateRequired(String value, String fieldLabel) {
        if (isBlank(value)) {
            throw new IllegalArgumentException("Le champ \"" + fieldLabel + "\" est obligatoire.");
        }
    }

    public static void validateRequired(Object value, String fieldLabel) {
        if (value == null) {
            throw new IllegalArgumentException("Le champ \"" + fieldLabel + "\" est obligatoire.");
        }
    }
}
