package com.artkids.controller.backoffice;

import com.artkids.config.AppConfig;
import com.artkids.model.Activity;
import com.artkids.model.Child;
import com.artkids.model.Reservation;
import com.artkids.model.User;
import com.artkids.service.ActivityService;
import com.artkids.service.CategoryService;
import com.artkids.service.ChildService;
import com.artkids.service.ReservationService;
import com.artkids.service.UserService;
import com.artkids.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Comparator;

public class AdminDashboardController {
    private final UserService userService = AppConfig.getInstance().getUserService();
    private final ChildService childService = AppConfig.getInstance().getChildService();
    private final CategoryService categoryService = AppConfig.getInstance().getCategoryService();
    private final ActivityService activityService = AppConfig.getInstance().getActivityService();
    private final ReservationService reservationService = AppConfig.getInstance().getReservationService();

    @FXML
    private Label parentCountLabel;
    @FXML
    private Label childCountLabel;
    @FXML
    private Label categoryCountLabel;
    @FXML
    private Label activityCountLabel;
    @FXML
    private Label reservationCountLabel;
    @FXML
    private Label currentAdminLabel;
    @FXML
    private ListView<String> recentReservationsList;
    @FXML
    private ListView<String> recentActivitiesList;

    @FXML
    public void initialize() {
        System.out.println("Admin dashboard initialized");
        if (AppConfig.getInstance().getCurrentUser() == null) {
            SceneManager.getInstance().showLogin();
            return;
        }
        refresh();
    }

    private void refresh() {
        User currentUser = AppConfig.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }

        currentAdminLabel.setText(currentUser.getFullName().trim());
        parentCountLabel.setText(String.valueOf(userService.countParents()));
        childCountLabel.setText(String.valueOf(childService.findAll().size()));
        categoryCountLabel.setText(String.valueOf(categoryService.findAll().size()));
        activityCountLabel.setText(String.valueOf(activityService.findAll().size()));
        reservationCountLabel.setText(String.valueOf(reservationService.findAll().size()));

        recentReservationsList.getItems().setAll(reservationService.findAll().stream()
                .sorted(Comparator.comparing(Reservation::getDateReservation).reversed())
                .limit(6)
                .map(reservation -> {
                    Child child = childService.findById(reservation.getChildId()).orElse(null);
                    Activity activity = activityService.findById(reservation.getActivityId()).orElse(null);
                    String childName = child == null ? "Enfant inconnu" : child.getFullName().trim();
                    String activityTitle = activity == null ? "Activite inconnue" : activity.getTitre();
                    return childName + " • " + activityTitle + " • " + reservation.getStatut();
                })
                .toList());

        recentActivitiesList.getItems().setAll(activityService.findAll().stream()
                .sorted(Comparator.comparing(Activity::getDateActivite))
                .limit(6)
                .map(activity -> activity.getTitre() + " • " + activity.getDateActivite() + " • " + activity.getStatut())
                .toList());
    }

    @FXML
    private void goToDashboard() {
        refresh();
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
    private void goToReservations() {
        SceneManager.getInstance().showAdminReservations();
    }

    @FXML
    private void logout() {
        AppConfig.getInstance().getAuthService().logout();
        SceneManager.getInstance().showLogin();
    }
}
