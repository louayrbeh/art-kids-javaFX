package com.artkids.controller.frontoffice;

import com.artkids.config.AppConfig;
import com.artkids.model.Activity;
import com.artkids.model.Category;
import com.artkids.service.ActivityService;
import com.artkids.service.CategoryService;
import com.artkids.util.AlertUtils;
import com.artkids.util.DateUtils;
import com.artkids.util.SceneManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ParentActivityController {
    private final ActivityService activityService = AppConfig.getInstance().getActivityService();
    private final CategoryService categoryService = AppConfig.getInstance().getCategoryService();

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private TextField ageField;
    @FXML
    private TableView<Activity> activitiesTable;
    @FXML
    private TableColumn<Activity, String> titreColumn;
    @FXML
    private TableColumn<Activity, String> categoryColumn;
    @FXML
    private TableColumn<Activity, String> dateColumn;
    @FXML
    private TableColumn<Activity, String> timeColumn;
    @FXML
    private TableColumn<Activity, String> ageRangeColumn;
    @FXML
    private TableColumn<Activity, Number> placesColumn;
    @FXML
    private TableColumn<Activity, String> statutColumn;
    @FXML
    private TableColumn<Activity, Void> actionColumn;

    @FXML
    public void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(categoryService.findAll()));
        categoryComboBox.setPromptText("Toutes les categories");

        titreColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitre()));
        categoryColumn.setCellValueFactory(cell -> new SimpleStringProperty(getCategoryName(cell.getValue().getCategoryId())));
        dateColumn.setCellValueFactory(cell -> new SimpleStringProperty(DateUtils.formatDate(cell.getValue().getDateActivite())));
        timeColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                DateUtils.formatTime(cell.getValue().getHeureDebut()) + " - " + DateUtils.formatTime(cell.getValue().getHeureFin())));
        ageRangeColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getAgeMin() + " - " + cell.getValue().getAgeMax() + " ans"));
        placesColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(
                activityService.getAvailablePlaces(cell.getValue().getId())));
        statutColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatut().toString()));
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button detailsButton = new Button("Details");

            {
                detailsButton.getStyleClass().add("primary-button");
                detailsButton.setOnAction(event ->
                        SceneManager.getInstance().showParentActivityDetails(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : detailsButton);
            }
        });

        applyFilters();
    }

    @FXML
    private void applyFilters() {
        try {
            final String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
            final Category selectedCategory = categoryComboBox.getValue();
            Integer parsedAge = null;
            if (ageField.getText() != null && !ageField.getText().isBlank()) {
                parsedAge = Integer.parseInt(ageField.getText().trim());
            }
            final Integer age = parsedAge;

            activitiesTable.getItems().setAll(activityService.findOpenActivities().stream()
                    .filter(activity -> query.isBlank() || activity.getTitre().toLowerCase().contains(query))
                    .filter(activity -> selectedCategory == null || activity.getCategoryId() == selectedCategory.getId())
                    .filter(activity -> age == null || (age >= activity.getAgeMin() && age <= activity.getAgeMax()))
                    .toList());
        } catch (NumberFormatException exception) {
            AlertUtils.showError("Le filtre age doit etre un nombre entier.");
        }
    }

    @FXML
    private void resetFilters() {
        searchField.clear();
        categoryComboBox.setValue(null);
        ageField.clear();
        applyFilters();
    }

    @FXML
    private void goToDashboard() {
        SceneManager.getInstance().showParentDashboard();
    }

    @FXML
    private void goToChildren() {
        SceneManager.getInstance().showParentChildren();
    }

    @FXML
    private void goToReservations() {
        SceneManager.getInstance().showParentReservations();
    }

    @FXML
    private void logout() {
        AppConfig.getInstance().getAuthService().logout();
        SceneManager.getInstance().showLogin();
    }

    private String getCategoryName(int categoryId) {
        return categoryService.findById(categoryId).map(Category::getNom).orElse("Categorie inconnue");
    }
}
