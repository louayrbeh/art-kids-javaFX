package com.artkids.controller.backoffice;

import com.artkids.config.AppConfig;
import com.artkids.model.Child;
import com.artkids.model.User;
import com.artkids.service.ChildService;
import com.artkids.service.UserService;
import com.artkids.util.DateUtils;
import com.artkids.util.SceneManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AdminChildController {
    private final ChildService childService = AppConfig.getInstance().getChildService();
    private final UserService userService = AppConfig.getInstance().getUserService();

    @FXML
    private TableView<Child> childTable;
    @FXML
    private TableColumn<Child, String> fullNameColumn;
    @FXML
    private TableColumn<Child, String> parentColumn;
    @FXML
    private TableColumn<Child, String> birthDateColumn;
    @FXML
    private TableColumn<Child, Number> ageColumn;
    @FXML
    private TableColumn<Child, String> sexeColumn;

    @FXML
    public void initialize() {
        fullNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFullName().trim()));
        parentColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                userService.findById(cell.getValue().getParentId())
                        .map(User::getFullName)
                        .orElse("Parent inconnu")
                        .trim()));
        birthDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(DateUtils.formatDate(cell.getValue().getDateNaissance())));
        ageColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getAge()));
        sexeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSexe().toString()));
        childTable.getItems().setAll(childService.findAll());
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
    private void goToUsers() {
        SceneManager.getInstance().showAdminUsers();
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
