package com.artkids.controller.auth;

import com.artkids.config.AppConfig;
import com.artkids.model.User;
import com.artkids.service.UserService;
import com.artkids.util.AlertUtils;
import com.artkids.util.SceneManager;
import com.artkids.util.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    private final UserService userService = AppConfig.getInstance().getUserService();

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telephoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void handleRegister() {
        try {
            String nom = requiredValue(nomField.getText(), "Veuillez saisir votre nom.");
            String prenom = requiredValue(prenomField.getText(), "Veuillez saisir votre prenom.");
            String email = requiredValue(emailField.getText(), "Veuillez saisir votre email.");
            String telephone = optionalValue(telephoneField.getText());
            String password = requiredValue(passwordField.getText(), "Veuillez saisir votre mot de passe.");
            String confirmation = requiredValue(
                    confirmPasswordField.getText(),
                    "Veuillez confirmer votre mot de passe."
            );

            if (!ValidationUtils.isValidEmail(email)) {
                throw new IllegalArgumentException("Veuillez saisir un email valide.");
            }
            if (userService.emailExists(email)) {
                throw new IllegalArgumentException("Cet email est deja utilise.");
            }
            if (password.length() < 6) {
                throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caracteres.");
            }
            if (!password.equals(confirmation)) {
                throw new IllegalArgumentException("Les mots de passe ne correspondent pas.");
            }

            User createdUser = userService.createParentAccount(nom, prenom, email, telephone, password);
            System.out.println("Compte parent cree : " + createdUser.getEmail());
            AlertUtils.showSuccess("Compte parent cree avec succes. Vous pouvez maintenant vous connecter.");
            SceneManager.getInstance().showLogin();
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
    }

    @FXML
    private void backToLogin() {
        SceneManager.getInstance().showLogin();
    }

    private String requiredValue(String value, String message) {
        if (ValidationUtils.isBlank(value)) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private String optionalValue(String value) {
        return ValidationUtils.isBlank(value) ? "" : value.trim();
    }
}
