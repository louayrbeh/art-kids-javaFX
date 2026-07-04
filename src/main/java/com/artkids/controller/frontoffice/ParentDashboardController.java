package com.artkids.controller.frontoffice;

import com.artkids.config.AppConfig;
import com.artkids.model.Activity;
import com.artkids.model.Child;
import com.artkids.model.Reservation;
import com.artkids.model.User;
import com.artkids.service.ActivityService;
import com.artkids.service.ChildService;
import com.artkids.service.ReservationService;
import com.artkids.util.SceneManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Comparator;

public class ParentDashboardController {
    private final ChildService childService = AppConfig.getInstance().getChildService();
    private final ActivityService activityService = AppConfig.getInstance().getActivityService();
    private final ReservationService reservationService = AppConfig.getInstance().getReservationService();

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label childrenCountLabel;
    @FXML
    private Label reservationsCountLabel;
    @FXML
    private Label nextActivitiesCountLabel;
    @FXML
    private Label availableActivitiesCountLabel;
    @FXML
    private ListView<String> nextActivitiesList;
    @FXML
    private ListView<String> recentReservationsList;
    @FXML
    private ListView<String> childrenList;

    @FXML
    public void initialize() {
        refresh();
    }

    private void refresh() {
        User currentUser = AppConfig.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }

        ObservableList<Child> children = childService.findByParent(currentUser.getId());
        ObservableList<Reservation> reservations = reservationService.findByParent(currentUser.getId());
        ObservableList<Activity> openActivities = activityService.findOpenActivities();

        welcomeLabel.setText("Bienvenue " + currentUser.getFullName().trim());
        childrenCountLabel.setText(String.valueOf(children.size()));
        reservationsCountLabel.setText(String.valueOf(reservations.size()));
        long nextActivities = reservations.stream()
                .map(reservation -> activityService.findById(reservation.getActivityId()).orElse(null))
                .filter(activity -> activity != null && activity.isFuture())
                .count();
        nextActivitiesCountLabel.setText(String.valueOf(nextActivities));
        availableActivitiesCountLabel.setText(String.valueOf(openActivities.size()));

        childrenList.getItems().setAll(children.stream()
                .map(child -> child.getFullName().trim() + " • " + child.getAge() + " ans")
                .sorted()
                .toList());

        nextActivitiesList.getItems().setAll(reservations.stream()
                .map(reservation -> activityService.findById(reservation.getActivityId()).orElse(null))
                .filter(activity -> activity != null && activity.isFuture())
                .sorted(Comparator.comparing(Activity::getDateActivite))
                .limit(5)
                .map(activity -> activity.getTitre() + " • " + activity.getDateActivite())
                .toList());

        recentReservationsList.getItems().setAll(reservations.stream()
                .sorted(Comparator.comparing(Reservation::getDateReservation).reversed())
                .limit(5)
                .map(reservation -> {
                    Child child = childService.findById(reservation.getChildId()).orElse(null);
                    Activity activity = activityService.findById(reservation.getActivityId()).orElse(null);
                    String childName = child == null ? "Enfant inconnu" : child.getFullName().trim();
                    String activityTitle = activity == null ? "Activite inconnue" : activity.getTitre();
                    return childName + " • " + activityTitle + " • " + reservation.getStatut();
                })
                .toList());
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
    private void goToReservations() {
        SceneManager.getInstance().showParentReservations();
    }

    @FXML
    private void logout() {
        AppConfig.getInstance().getAuthService().logout();
        SceneManager.getInstance().showLogin();
    }
}
