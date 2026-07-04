package com.artkids.controller.frontoffice;

import com.artkids.config.AppConfig;
import com.artkids.model.Activity;
import com.artkids.model.Category;
import com.artkids.model.Child;
import com.artkids.service.ActivityService;
import com.artkids.service.CategoryService;
import com.artkids.service.ChildService;
import com.artkids.service.ReservationService;
import com.artkids.util.AlertUtils;
import com.artkids.util.DataReceiver;
import com.artkids.util.DateUtils;
import com.artkids.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ParentActivityDetailsController implements DataReceiver<Activity> {
    private final ActivityService activityService = AppConfig.getInstance().getActivityService();
    private final CategoryService categoryService = AppConfig.getInstance().getCategoryService();
    private final ChildService childService = AppConfig.getInstance().getChildService();
    private final ReservationService reservationService = AppConfig.getInstance().getReservationService();

    private Activity activity;

    @FXML
    private Label titleLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label placeLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private ComboBox<Child> childComboBox;
    @FXML
    private ListView<String> compatibilityList;

    @FXML
    public void initialize() {
        childComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Child child) {
                return child == null ? "" : child.getFullName().trim() + " (" + child.getAge() + " ans)";
            }

            @Override
            public Child fromString(String string) {
                return null;
            }
        });
    }

    @Override
    public void setData(Activity activity) {
        this.activity = activityService.findById(activity.getId()).orElse(activity);
        populateView();
    }

    @FXML
    private void reserveActivity() {
        if (activity == null) {
            return;
        }
        Child selectedChild = childComboBox.getValue();
        if (selectedChild == null) {
            AlertUtils.showError("Veuillez selectionner un enfant.");
            return;
        }
        try {
            reservationService.createReservation(selectedChild.getId(), activity.getId());
            AlertUtils.showSuccess("Reservation creee avec succes.");
            populateView();
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
    }

    @FXML
    private void backToActivities() {
        SceneManager.getInstance().showParentActivities();
    }

    private void populateView() {
        if (activity == null) {
            return;
        }
        Category category = categoryService.findById(activity.getCategoryId()).orElse(null);
        titleLabel.setText(activity.getTitre());
        categoryLabel.setText(category == null ? "-" : category.getNom());
        dateLabel.setText(DateUtils.formatDate(activity.getDateActivite()));
        timeLabel.setText(DateUtils.formatTime(activity.getHeureDebut()) + " - " + DateUtils.formatTime(activity.getHeureFin()));
        placeLabel.setText(activityService.getAvailablePlaces(activity.getId()) + " / " + activity.getCapaciteMax());
        ageLabel.setText(activity.getAgeMin() + " - " + activity.getAgeMax() + " ans");
        priceLabel.setText(activity.getPrix() + " TND");
        statusLabel.setText(activity.getStatut().toString());
        locationLabel.setText(activity.getLieu());
        descriptionLabel.setText(activity.getDescription());

        int parentId = AppConfig.getInstance().getCurrentUser().getId();
        childComboBox.setItems(FXCollections.observableArrayList(childService.findByParent(parentId)));
        compatibilityList.getItems().setAll(childService.findByParent(parentId).stream()
                .map(child -> {
                    boolean compatible = child.getAge() >= activity.getAgeMin() && child.getAge() <= activity.getAgeMax();
                    return child.getFullName().trim() + " • " + (compatible ? "Compatible" : "Age incompatible");
                })
                .toList());
    }
}
