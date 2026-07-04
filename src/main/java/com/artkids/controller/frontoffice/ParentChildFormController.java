package com.artkids.controller.frontoffice;

import com.artkids.config.AppConfig;
import com.artkids.enums.Sexe;
import com.artkids.model.Child;
import com.artkids.service.ChildService;
import com.artkids.util.AlertUtils;
import com.artkids.util.DataReceiver;
import com.artkids.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ParentChildFormController implements DataReceiver<Child> {
    private final ChildService childService = AppConfig.getInstance().getChildService();
    private Child editingChild;

    @FXML
    private Label pageTitleLabel;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private ComboBox<Sexe> sexeComboBox;

    @FXML
    public void initialize() {
        sexeComboBox.setItems(FXCollections.observableArrayList(Sexe.values()));
        updateTitle();
    }

    @Override
    public void setData(Child child) {
        this.editingChild = child;
        if (child != null) {
            nomField.setText(child.getNom());
            prenomField.setText(child.getPrenom());
            birthDatePicker.setValue(child.getDateNaissance());
            sexeComboBox.setValue(child.getSexe());
        }
        updateTitle();
    }

    @FXML
    private void saveChild() {
        try {
            Child child = editingChild == null ? new Child() : new Child();
            if (editingChild != null) {
                child.setId(editingChild.getId());
            }
            child.setNom(nomField.getText());
            child.setPrenom(prenomField.getText());
            child.setDateNaissance(birthDatePicker.getValue());
            child.setSexe(sexeComboBox.getValue());

            if (editingChild == null) {
                childService.create(child);
                AlertUtils.showSuccess("Enfant ajoute avec succes.");
            } else {
                childService.update(child);
                AlertUtils.showSuccess("Enfant mis a jour avec succes.");
            }
            SceneManager.getInstance().showParentChildren();
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
    }

    @FXML
    private void cancel() {
        SceneManager.getInstance().showParentChildren();
    }

    private void updateTitle() {
        if (pageTitleLabel != null) {
            pageTitleLabel.setText(editingChild == null ? "Ajouter un enfant" : "Modifier un enfant");
        }
    }
}
