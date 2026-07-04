package com.artkids.controller.backoffice;

import com.artkids.config.AppConfig;
import com.artkids.model.User;
import com.artkids.service.UserService;
import com.artkids.util.AlertUtils;
import com.artkids.util.DateUtils;
import com.artkids.util.SceneManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public class AdminUserController {
    private final UserService userService = AppConfig.getInstance().getUserService();

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> phoneColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> statusColumn;
    @FXML
    private TableColumn<User, String> createdAtColumn;
    @FXML
    private TableColumn<User, Void> actionColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFullName().trim()));
        emailColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        phoneColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTelephone()));
        roleColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRole().toString()));
        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isActive() ? "Actif" : "Desactive"));
        createdAtColumn.setCellValueFactory(cell -> new SimpleStringProperty(DateUtils.formatDateTime(cell.getValue().getCreatedAt())));
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button toggleButton = new Button("Activer/Desactiver");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox box = new HBox(8, editButton, toggleButton, deleteButton);

            {
                editButton.getStyleClass().add("secondary-button");
                toggleButton.getStyleClass().add("primary-button");
                deleteButton.getStyleClass().add("danger-button");
                editButton.setOnAction(event ->
                        SceneManager.getInstance().showAdminUserForm(getTableView().getItems().get(getIndex())));
                toggleButton.setOnAction(event -> toggleStatus(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> deleteUser(getTableView().getItems().get(getIndex())));
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
        userTable.getItems().setAll(userService.findAll());
    }

    private void toggleStatus(User user) {
        try {
            userService.toggleStatus(user.getId());
            refreshTable();
            AlertUtils.showSuccess("Statut utilisateur mis a jour.");
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
    }

    private void deleteUser(User user) {
        if (!AlertUtils.showConfirmation("Supprimer le compte " + user.getFullName().trim() + " ?")) {
            return;
        }
        try {
            boolean deleted = userService.delete(user.getId());
            refreshTable();
            if (deleted) {
                AlertUtils.showSuccess("Utilisateur supprime avec succes.");
            } else {
                AlertUtils.showWarning("L'utilisateur possede des enfants : le compte a ete desactive.");
            }
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
    }

    @FXML
    private void addUser() {
        SceneManager.getInstance().showAdminUserForm(null);
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
    private void goToActivities() {
        SceneManager.getInstance().showAdminActivities();
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
