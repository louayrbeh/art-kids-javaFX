package com.artkids.controller.backoffice;

import com.artkids.config.AppConfig;
import com.artkids.enums.UserRole;
import com.artkids.model.User;
import com.artkids.service.UserService;
import com.artkids.util.AlertUtils;
import com.artkids.util.DataReceiver;
import com.artkids.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminUserFormController implements DataReceiver<User> {
    private final UserService userService = AppConfig.getInstance().getUserService();
    private User editingUser;

    @FXML
    private Label pageTitleLabel;
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
    private ComboBox<UserRole> roleComboBox;
    @FXML
    private CheckBox activeCheckBox;

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList(UserRole.values()));
        activeCheckBox.setSelected(true);
        updateTitle();
    }

    @Override
    public void setData(User user) {
        this.editingUser = user;
        if (user != null) {
            nomField.setText(user.getNom());
            prenomField.setText(user.getPrenom());
            emailField.setText(user.getEmail());
            telephoneField.setText(user.getTelephone());
            roleComboBox.setValue(user.getRole());
            activeCheckBox.setSelected(user.isActive());
        }
        updateTitle();
    }

    @FXML
    private void saveUser() {
        try {
            User user = new User();
            if (editingUser != null) {
                user.setId(editingUser.getId());
            }
            user.setNom(nomField.getText());
            user.setPrenom(prenomField.getText());
            user.setEmail(emailField.getText());
            user.setTelephone(telephoneField.getText());
            user.setPassword(passwordField.getText());
            user.setRole(roleComboBox.getValue());
            user.setActive(activeCheckBox.isSelected());

            if (editingUser == null) {
                userService.create(user);
                AlertUtils.showSuccess("Utilisateur ajoute avec succes.");
            } else {
                userService.update(user);
                AlertUtils.showSuccess("Utilisateur mis a jour avec succes.");
            }
            SceneManager.getInstance().showAdminUsers();
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
    }

    @FXML
    private void cancel() {
        SceneManager.getInstance().showAdminUsers();
    }

    private void updateTitle() {
        if (pageTitleLabel != null) {
            pageTitleLabel.setText(editingUser == null ? "Ajouter un utilisateur" : "Modifier un utilisateur");
        }
    }
}
