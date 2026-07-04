package com.artkids.controller.frontoffice;

import com.artkids.config.AppConfig;
import com.artkids.model.Child;
import com.artkids.service.ChildService;
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

public class ParentChildController {
    private final ChildService childService = AppConfig.getInstance().getChildService();

    @FXML
    private TableView<Child> childrenTable;
    @FXML
    private TableColumn<Child, String> nomColumn;
    @FXML
    private TableColumn<Child, String> prenomColumn;
    @FXML
    private TableColumn<Child, String> birthDateColumn;
    @FXML
    private TableColumn<Child, Number> ageColumn;
    @FXML
    private TableColumn<Child, String> sexeColumn;
    @FXML
    private TableColumn<Child, Void> actionsColumn;

    @FXML
    public void initialize() {
        nomColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNom()));
        prenomColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPrenom()));
        birthDateColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(DateUtils.formatDate(cell.getValue().getDateNaissance())));
        ageColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getAge()));
        sexeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSexe().toString()));
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox box = new HBox(8, editButton, deleteButton);

            {
                editButton.getStyleClass().add("secondary-button");
                deleteButton.getStyleClass().add("danger-button");
                editButton.setOnAction(event -> SceneManager.getInstance().showParentChildForm(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> deleteChild(getTableView().getItems().get(getIndex())));
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
        int parentId = AppConfig.getInstance().getCurrentUser().getId();
        childrenTable.getItems().setAll(childService.findByParent(parentId));
    }

    private void deleteChild(Child child) {
        if (!AlertUtils.showConfirmation("Supprimer " + child.getFullName().trim() + " ?")) {
            return;
        }
        try {
            childService.delete(child.getId());
            AppConfig.getInstance().getActivityService().refreshStatuses();
            refreshTable();
            AlertUtils.showSuccess("Enfant supprime avec succes.");
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
    }

    @FXML
    private void addChild() {
        SceneManager.getInstance().showParentChildForm(null);
    }

    @FXML
    private void backToDashboard() {
        SceneManager.getInstance().showParentDashboard();
    }

    @FXML
    private void goToActivities() {
        SceneManager.getInstance().showParentActivities();
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
}
