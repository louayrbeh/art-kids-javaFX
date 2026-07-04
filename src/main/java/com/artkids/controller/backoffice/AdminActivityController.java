package com.artkids.controller.backoffice;

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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public class AdminActivityController {
    private final ActivityService activityService = AppConfig.getInstance().getActivityService();
    private final CategoryService categoryService = AppConfig.getInstance().getCategoryService();

    @FXML
    private TableView<Activity> activityTable;
    @FXML
    private TableColumn<Activity, String> titleColumn;
    @FXML
    private TableColumn<Activity, String> categoryColumn;
    @FXML
    private TableColumn<Activity, String> dateColumn;
    @FXML
    private TableColumn<Activity, Number> capacityColumn;
    @FXML
    private TableColumn<Activity, String> statusColumn;
    @FXML
    private TableColumn<Activity, Void> actionColumn;

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitre()));
        categoryColumn.setCellValueFactory(cell -> new SimpleStringProperty(getCategoryName(cell.getValue().getCategoryId())));
        dateColumn.setCellValueFactory(cell -> new SimpleStringProperty(DateUtils.formatDate(cell.getValue().getDateActivite())));
        capacityColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getCapaciteMax()));
        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatut().toString()));
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox box = new HBox(8, editButton, deleteButton);

            {
                editButton.getStyleClass().add("secondary-button");
                deleteButton.getStyleClass().add("danger-button");
                editButton.setOnAction(event ->
                        SceneManager.getInstance().showAdminActivityForm(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> deleteActivity(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
        refreshTable();
    }

    private void refreshTable() {
        activityTable.getItems().setAll(activityService.findAll());
    }

    private void deleteActivity(Activity activity) {
        if (!AlertUtils.showConfirmation("Supprimer l'activite \"" + activity.getTitre() + "\" ?")) {
            return;
        }
        try {
            activityService.delete(activity.getId());
            refreshTable();
            AlertUtils.showSuccess("Activite supprimee avec succes.");
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
    }

    @FXML
    private void addActivity() {
        SceneManager.getInstance().showAdminActivityForm(null);
    }

    @FXML
    private void goToDashboard() {
        SceneManager.getInstance().showAdminDashboard();
    }

    @FXML
    private void goToCategories() {
        SceneManager.getInstance().showAdminCategories();
    }

    @FXML
    private void goToUsers() {
        SceneManager.getInstance().showAdminUsers();
    }

    @FXML
    private void goToChildren() {
        SceneManager.getInstance().showAdminChildren();
    }

    @FXML
    private void goToReservations() {
        SceneManager.getInstance().showAdminReservations();
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
