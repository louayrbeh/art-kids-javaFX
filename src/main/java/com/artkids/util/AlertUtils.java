package com.artkids.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public final class AlertUtils {
    private AlertUtils() {
    }

    public static void showSuccess(String message) {
        showAlert(Alert.AlertType.INFORMATION, "Succes", message);
    }

    public static void showError(String message) {
        showAlert(Alert.AlertType.ERROR, "Erreur", message);
    }

    public static void showWarning(String message) {
        showAlert(Alert.AlertType.WARNING, "Attention", message);
    }

    public static boolean showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
