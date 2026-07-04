package com.artkids.controller.backoffice;

import com.artkids.config.AppConfig;
import com.artkids.model.Activity;
import com.artkids.model.Child;
import com.artkids.model.Reservation;
import com.artkids.model.User;
import com.artkids.service.ActivityService;
import com.artkids.service.ChildService;
import com.artkids.service.ReservationService;
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

public class AdminReservationController {
    private final ReservationService reservationService = AppConfig.getInstance().getReservationService();
    private final ChildService childService = AppConfig.getInstance().getChildService();
    private final ActivityService activityService = AppConfig.getInstance().getActivityService();
    private final UserService userService = AppConfig.getInstance().getUserService();

    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private TableColumn<Reservation, String> parentColumn;
    @FXML
    private TableColumn<Reservation, String> childColumn;
    @FXML
    private TableColumn<Reservation, String> activityColumn;
    @FXML
    private TableColumn<Reservation, String> dateColumn;
    @FXML
    private TableColumn<Reservation, String> statusColumn;
    @FXML
    private TableColumn<Reservation, Void> actionColumn;

    @FXML
    public void initialize() {
        parentColumn.setCellValueFactory(cell -> new SimpleStringProperty(getParentName(cell.getValue().getChildId())));
        childColumn.setCellValueFactory(cell -> new SimpleStringProperty(getChildName(cell.getValue().getChildId())));
        activityColumn.setCellValueFactory(cell -> new SimpleStringProperty(getActivityTitle(cell.getValue().getActivityId())));
        dateColumn.setCellValueFactory(cell -> new SimpleStringProperty(DateUtils.formatDateTime(cell.getValue().getDateReservation())));
        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatut().toString()));
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button confirmButton = new Button("Confirmer");
            private final Button cancelButton = new Button("Annuler");
            private final HBox box = new HBox(8, confirmButton, cancelButton);

            {
                confirmButton.getStyleClass().add("success-button");
                cancelButton.getStyleClass().add("danger-button");
                confirmButton.setOnAction(event -> confirmReservation(getTableView().getItems().get(getIndex())));
                cancelButton.setOnAction(event -> cancelReservation(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                Reservation reservation = getTableView().getItems().get(getIndex());
                confirmButton.setDisable(!reservationService.canConfirm(reservation));
                cancelButton.setDisable(!reservationService.canCancel(reservation));
                setGraphic(box);
            }
        });
        refreshTable();
    }

    private void refreshTable() {
        reservationTable.getItems().setAll(reservationService.findAll());
    }

    private void confirmReservation(Reservation reservation) {
        try {
            reservationService.confirmReservation(reservation.getId());
            refreshTable();
            AlertUtils.showSuccess("Reservation confirmee.");
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
    }

    private void cancelReservation(Reservation reservation) {
        if (!AlertUtils.showConfirmation("Annuler cette reservation ?")) {
            return;
        }
        try {
            reservationService.cancelReservation(reservation.getId());
            refreshTable();
            AlertUtils.showSuccess("Reservation annulee.");
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
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
    private void goToChildren() {
        SceneManager.getInstance().showAdminChildren();
    }

    @FXML
    private void logout() {
        AppConfig.getInstance().getAuthService().logout();
        SceneManager.getInstance().showLogin();
    }

    private String getParentName(int childId) {
        Child child = childService.findById(childId).orElse(null);
        if (child == null) {
            return "Parent inconnu";
        }
        return userService.findById(child.getParentId()).map(User::getFullName).orElse("Parent inconnu").trim();
    }

    private String getChildName(int childId) {
        return childService.findById(childId).map(Child::getFullName).orElse("Enfant inconnu").trim();
    }

    private String getActivityTitle(int activityId) {
        return activityService.findById(activityId).map(Activity::getTitre).orElse("Activite inconnue");
    }
}
