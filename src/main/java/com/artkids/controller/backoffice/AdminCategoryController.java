package com.artkids.controller.backoffice;

import com.artkids.config.AppConfig;
import com.artkids.model.Category;
import com.artkids.service.ActivityService;
import com.artkids.service.CategoryService;
import com.artkids.util.AlertUtils;
import com.artkids.util.SceneManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public class AdminCategoryController {
    private final CategoryService categoryService = AppConfig.getInstance().getCategoryService();
    private final ActivityService activityService = AppConfig.getInstance().getActivityService();

    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, String> imageColumn;
    @FXML
    private TableColumn<Category, String> nomColumn;
    @FXML
    private TableColumn<Category, String> descriptionColumn;
    @FXML
    private TableColumn<Category, Number> activitiesCountColumn;
    @FXML
    private TableColumn<Category, Void> actionColumn;

    @FXML
    public void initialize() {
        imageColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getImage()));
        nomColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNom()));
        descriptionColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));
        activitiesCountColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(
                (int) activityService.findAll().stream()
                        .filter(activity -> activity.getCategoryId() == cell.getValue().getId())
                        .count()
        ));
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox box = new HBox(8, editButton, deleteButton);

            {
                editButton.getStyleClass().add("secondary-button");
                deleteButton.getStyleClass().add("danger-button");
                editButton.setOnAction(event ->
                        SceneManager.getInstance().showAdminCategoryForm(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> deleteCategory(getTableView().getItems().get(getIndex())));
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
        categoryTable.getItems().setAll(categoryService.findAll());
    }

    private void deleteCategory(Category category) {
        if (!AlertUtils.showConfirmation("Supprimer la categorie \"" + category.getNom() + "\" ?")) {
            return;
        }
        try {
            categoryService.delete(category.getId());
            refreshTable();
            AlertUtils.showSuccess("Categorie supprimee avec succes.");
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
    }

    @FXML
    private void addCategory() {
        SceneManager.getInstance().showAdminCategoryForm(null);
    }

    @FXML
    private void goToDashboard() {
        SceneManager.getInstance().showAdminDashboard();
    }

    @FXML
    private void goToActivities() {
        SceneManager.getInstance().showAdminActivities();
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
}
