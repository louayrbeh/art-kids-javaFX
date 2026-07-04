package com.artkids.controller.frontoffice;

import com.artkids.config.AppConfig;
import com.artkids.model.Activity;
import com.artkids.model.Child;
import com.artkids.model.Reservation;
import com.artkids.service.ActivityService;
import com.artkids.service.ChildService;
import com.artkids.service.ReservationService;
import com.artkids.util.AlertUtils;
import com.artkids.util.DateUtils;
import com.artkids.util.SceneManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ParentReservationController {
    private final ReservationService reservationService = AppConfig.getInstance().getReservationService();
    private final ChildService childService = AppConfig.getInstance().getChildService();
    private final ActivityService activityService = AppConfig.getInstance().getActivityService();

    @FXML
    private TableView<Reservation> reservationsTable;
    @FXML
    private TableColumn<Reservation, String> childColumn;
    @FXML
    private TableColumn<Reservation, String> activityColumn;
    @FXML
    private TableColumn<Reservation, String> activityDateColumn;
    @FXML
    private TableColumn<Reservation, String> reservationDateColumn;
    @FXML
    private TableColumn<Reservation, String> statusColumn;
    @FXML
    private TableColumn<Reservation, Void> actionColumn;

    @FXML
    public void initialize() {
        childColumn.setCellValueFactory(cell -> new SimpleStringProperty(getChildName(cell.getValue().getChildId())));
        activityColumn.setCellValueFactory(cell -> new SimpleStringProperty(getActivityTitle(cell.getValue().getActivityId())));
        activityDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                activityService.findById(cell.getValue().getActivityId())
                        .map(Activity::getDateActivite)
                        .map(DateUtils::formatDate)
                        .orElse("-")));
        reservationDateColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(DateUtils.formatDateTime(cell.getValue().getDateReservation())));
        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatut().toString()));
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button cancelButton = new Button("Annuler");

            {
                cancelButton.getStyleClass().add("danger-button");
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
                cancelButton.setDisable(!reservationService.canCancel(reservation));
                setGraphic(cancelButton);
            }
        });
        refreshTable();
    }

    private void refreshTable() {
        int parentId = AppConfig.getInstance().getCurrentUser().getId();
        reservationsTable.getItems().setAll(reservationService.findByParent(parentId));
    }

    private void cancelReservation(Reservation reservation) {
        if (!AlertUtils.showConfirmation("Annuler cette reservation ?")) {
            return;
        }
        try {
            reservationService.cancelReservation(reservation.getId());
            refreshTable();
            AlertUtils.showSuccess("Reservation annulee avec succes.");
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
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
    private void goToActivities() {
        SceneManager.getInstance().showParentActivities();
    }

    @FXML
    private void logout() {
        AppConfig.getInstance().getAuthService().logout();
        SceneManager.getInstance().showLogin();
    }

    private String getChildName(int childId) {
        return childService.findById(childId).map(Child::getFullName).orElse("Enfant inconnu").trim();
    }

    private String getActivityTitle(int activityId) {
        return activityService.findById(activityId).map(Activity::getTitre).orElse("Activite inconnue");
    }
}
