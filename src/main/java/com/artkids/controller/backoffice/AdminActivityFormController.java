package com.artkids.controller.backoffice;

import com.artkids.config.AppConfig;
import com.artkids.enums.ActivityStatus;
import com.artkids.model.Activity;
import com.artkids.model.Category;
import com.artkids.service.ActivityService;
import com.artkids.service.CategoryService;
import com.artkids.util.AlertUtils;
import com.artkids.util.DataReceiver;
import com.artkids.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AdminActivityFormController implements DataReceiver<Activity> {
    private final ActivityService activityService = AppConfig.getInstance().getActivityService();
    private final CategoryService categoryService = AppConfig.getInstance().getCategoryService();
    private Activity editingActivity;

    @FXML
    private Label pageTitleLabel;
    @FXML
    private TextField titreField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField imageField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField heureDebutField;
    @FXML
    private TextField heureFinField;
    @FXML
    private TextField capaciteField;
    @FXML
    private TextField ageMinField;
    @FXML
    private TextField ageMaxField;
    @FXML
    private TextField prixField;
    @FXML
    private ComboBox<ActivityStatus> statutComboBox;
    @FXML
    private TextField lieuField;
    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    public void initialize() {
        statutComboBox.setItems(FXCollections.observableArrayList(ActivityStatus.values()));
        categoryComboBox.setItems(FXCollections.observableArrayList(categoryService.findAll()));
        updateTitle();
    }

    @Override
    public void setData(Activity activity) {
        this.editingActivity = activity;
        if (activity != null) {
            titreField.setText(activity.getTitre());
            descriptionArea.setText(activity.getDescription());
            imageField.setText(activity.getImage());
            datePicker.setValue(activity.getDateActivite());
            heureDebutField.setText(activity.getHeureDebut().toString());
            heureFinField.setText(activity.getHeureFin().toString());
            capaciteField.setText(String.valueOf(activity.getCapaciteMax()));
            ageMinField.setText(String.valueOf(activity.getAgeMin()));
            ageMaxField.setText(String.valueOf(activity.getAgeMax()));
            prixField.setText(activity.getPrix().toPlainString());
            statutComboBox.setValue(activity.getStatut());
            lieuField.setText(activity.getLieu());
            categoryComboBox.setValue(categoryService.findById(activity.getCategoryId()).orElse(null));
        }
        updateTitle();
    }

    @FXML
    private void generateDescription() {
        String titre = titreField.getText() == null || titreField.getText().isBlank() ? "activite artistique" : titreField.getText().trim();
        String ageMin = ageMinField.getText() == null || ageMinField.getText().isBlank() ? "6" : ageMinField.getText().trim();
        String ageMax = ageMaxField.getText() == null || ageMaxField.getText().isBlank() ? "12" : ageMaxField.getText().trim();
        descriptionArea.setText("Une experience creatrice autour de " + titre
                + ", pensee pour les enfants de " + ageMin + " a " + ageMax
                + " ans, avec un accompagnement bienveillant et des objectifs ludiques.");
    }

    @FXML
    private void saveActivity() {
        try {
            Activity activity = new Activity();
            if (editingActivity != null) {
                activity.setId(editingActivity.getId());
            }
            activity.setTitre(titreField.getText());
            activity.setDescription(descriptionArea.getText());
            activity.setImage(imageField.getText());
            activity.setDateActivite(datePicker.getValue());
            activity.setHeureDebut(LocalTime.parse(heureDebutField.getText().trim()));
            activity.setHeureFin(LocalTime.parse(heureFinField.getText().trim()));
            activity.setCapaciteMax(Integer.parseInt(capaciteField.getText().trim()));
            activity.setAgeMin(Integer.parseInt(ageMinField.getText().trim()));
            activity.setAgeMax(Integer.parseInt(ageMaxField.getText().trim()));
            activity.setPrix(new BigDecimal(prixField.getText().trim()));
            activity.setStatut(statutComboBox.getValue());
            activity.setLieu(lieuField.getText());
            activity.setCategoryId(categoryComboBox.getValue() == null ? 0 : categoryComboBox.getValue().getId());

            if (editingActivity == null) {
                activityService.create(activity);
                AlertUtils.showSuccess("Activite ajoutee avec succes.");
            } else {
                activityService.update(activity);
                AlertUtils.showSuccess("Activite mise a jour avec succes.");
            }
            SceneManager.getInstance().showAdminActivities();
        } catch (NumberFormatException | DateTimeParseException exception) {
            AlertUtils.showError("Veuillez verifier les champs numeriques et les heures au format HH:mm.");
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
    }

    @FXML
    private void cancel() {
        SceneManager.getInstance().showAdminActivities();
    }

    private void updateTitle() {
        if (pageTitleLabel != null) {
            pageTitleLabel.setText(editingActivity == null ? "Ajouter une activite" : "Modifier une activite");
        }
    }
}
