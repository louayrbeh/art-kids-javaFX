package com.artkids.service;

import com.artkids.config.AppConfig;
import com.artkids.enums.UserRole;
import com.artkids.model.Activity;
import com.artkids.model.User;
import com.artkids.util.JsonUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

public class ActivityApiService extends ActivityService {
    protected final ApiClient apiClient;

    public ActivityApiService(ApiClient apiClient, MockDataService dataService) {
        super(dataService);
        this.apiClient = apiClient;
    }

    @Override
    public ObservableList<Activity> findAll() {
        return FXCollections.observableArrayList(ApiMappers.array(apiClient.get("/admin/activities")).stream()
                .map(ApiMappers::activity)
                .toList());
    }

    @Override
    public ObservableList<Activity> findOpenActivities() {
        return FXCollections.observableArrayList(ApiMappers.array(apiClient.get("/parent/activities")).stream()
                .map(ApiMappers::activity)
                .toList());
    }

    @Override
    public Optional<Activity> findById(int id) {
        try {
            return Optional.of(ApiMappers.activity(JsonUtils.parseObject(apiClient.get(activityPath(id)))));
        } catch (ApiException exception) {
            if (exception.getStatusCode() == 404) {
                return Optional.empty();
            }
            throw exception;
        }
    }

    @Override
    public Activity create(Activity activity) {
        String response = apiClient.post("/admin/activities", JsonUtils.stringify(ApiMappers.activityPayload(activity)));
        return ApiMappers.activity(JsonUtils.parseObject(response));
    }

    @Override
    public Activity update(Activity activity) {
        String response = apiClient.put("/admin/activities/" + activity.getId(),
                JsonUtils.stringify(ApiMappers.activityPayload(activity)));
        return ApiMappers.activity(JsonUtils.parseObject(response));
    }

    @Override
    public void delete(int id) {
        apiClient.delete("/admin/activities/" + id);
    }

    @Override
    public int countReservationsForActivity(int activityId) {
        return findById(activityId)
                .filter(activity -> activity.getPlacesDisponibles() >= 0)
                .map(activity -> Math.max(0, activity.getCapaciteMax() - activity.getPlacesDisponibles()))
                .orElse(0);
    }

    @Override
    public int getAvailablePlaces(int activityId) {
        return findById(activityId)
                .map(activity -> activity.getPlacesDisponibles() >= 0
                        ? activity.getPlacesDisponibles()
                        : Math.max(0, activity.getCapaciteMax() - countReservationsForActivity(activityId)))
                .orElse(0);
    }

    @Override
    public void refreshStatuses() {
        // Symfony owns activity status synchronization in API mode.
    }

    private String activityPath(int id) {
        User currentUser = AppConfig.getInstance().getCurrentUser();
        return currentUser != null && currentUser.getRole() == UserRole.ADMIN
                ? "/admin/activities/" + id
                : "/parent/activities/" + id;
    }
}
