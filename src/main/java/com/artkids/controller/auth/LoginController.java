package com.artkids.controller.auth;

import com.artkids.config.AppConfig;
import com.artkids.enums.UserRole;
import com.artkids.model.User;
import com.artkids.util.SceneManager;
import com.artkids.util.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        errorLabel.setText("");
        try {
            if (!ValidationUtils.isValidEmail(emailField.getText())) {
                throw new IllegalArgumentException("Veuillez saisir une adresse email valide.");
            }

            User user = AppConfig.getInstance().getAuthService().login(
                    emailField.getText().trim(),
                    passwordField.getText()
            );

            System.out.println("Utilisateur connecte : " + user.getEmail());
            System.out.println("Role : " + user.getRole());

            if (user.getRole() == UserRole.ADMIN) {
                SceneManager.getInstance().showAdminDashboard();
            } else if (user.getRole() == UserRole.PARENT) {
                SceneManager.getInstance().showParentDashboard();
            } else {
                throw new IllegalStateException("Role utilisateur non gere : " + user.getRole());
            }
        } catch (IllegalArgumentException exception) {
            errorLabel.setText(exception.getMessage());
        } catch (IllegalStateException exception) {
            errorLabel.setText("Impossible d'ouvrir l'espace demande.");
            throw exception;
        }
    }

    @FXML
    private void fillAdminDemo() {
        emailField.setText("admin@artkids.com");
        passwordField.setText("admin123");
        errorLabel.setText("");
    }

    @FXML
    private void fillParentDemo() {
        emailField.setText("parent@artkids.com");
        passwordField.setText("parent123");
        errorLabel.setText("");
    }

    @FXML
    private void showRegister() {
        errorLabel.setText("");
        SceneManager.getInstance().showRegister();
    }
}
