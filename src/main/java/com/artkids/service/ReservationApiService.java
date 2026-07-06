package com.artkids.service;

import com.artkids.config.AppConfig;
import com.artkids.enums.ReservationStatus;
import com.artkids.enums.UserRole;
import com.artkids.model.Activity;
import com.artkids.model.Reservation;
import com.artkids.model.User;
import com.artkids.util.JsonUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ReservationApiService extends ReservationService {
    protected final ApiClient apiClient;
    private final ActivityService activityService;

    public ReservationApiService(ApiClient apiClient, MockDataService dataService,
                                 ChildService childService, ActivityService activityService) {
        super(dataService, childService, activityService);
        this.apiClient = apiClient;
        this.activityService = activityService;
    }

    @Override
    public ObservableList<Reservation> findAll() {
        return FXCollections.observableArrayList(ApiMappers.array(apiClient.get("/admin/reservations")).stream()
                .map(ApiMappers::reservation)
                .toList());
    }

    @Override
    public Optional<Reservation> findById(int id) {
        try {
            return Optional.of(ApiMappers.reservation(JsonUtils.parseObject(apiClient.get(reservationPath(id)))));
        } catch (ApiException exception) {
            if (exception.getStatusCode() == 404) {
                return Optional.empty();
            }
            throw exception;
        }
    }

    @Override
    public ObservableList<Reservation> findByParent(int parentId) {
        User currentUser = AppConfig.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getRole() == UserRole.ADMIN) {
            return FXCollections.observableArrayList(findAll().stream().toList());
        }
        return FXCollections.observableArrayList(ApiMappers.array(apiClient.get("/parent/reservations")).stream()
                .map(ApiMappers::reservation)
                .toList());
    }

    @Override
    public ObservableList<Reservation> findByActivity(int activityId) {
        return FXCollections.observableArrayList(findAll().stream()
                .filter(reservation -> reservation.getActivityId() == activityId)
                .toList());
    }

    @Override
    public Reservation createReservation(int childId, int activityId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("childId", childId);
        payload.put("activityId", activityId);
        String response = apiClient.post("/parent/reservations", JsonUtils.stringify(payload));
        return ApiMappers.reservation(JsonUtils.parseObject(response));
    }

    @Override
    public Reservation cancelReservation(int reservationId) {
        String path = isAdmin() ? "/admin/reservations/" + reservationId + "/cancel"
                : "/parent/reservations/" + reservationId + "/cancel";
        return ApiMappers.reservation(JsonUtils.parseObject(apiClient.put(path, "{}")));
    }

    @Override
    public Reservation confirmReservation(int reservationId) {
        return ApiMappers.reservation(JsonUtils.parseObject(
                apiClient.put("/admin/reservations/" + reservationId + "/confirm", "{}")));
    }

    public Reservation finishReservation(int reservationId) {
        return ApiMappers.reservation(JsonUtils.parseObject(
                apiClient.put("/admin/reservations/" + reservationId + "/finish", "{}")));
    }

    @Override
    public boolean canCancel(Reservation reservation) {
        if (reservation.getStatut() == ReservationStatus.ANNULEE
                || reservation.getStatut() == ReservationStatus.TERMINEE) {
            return false;
        }
        if (isAdmin()) {
            return true;
        }
        Activity activity = activityService.findById(reservation.getActivityId()).orElse(null);
        return activity != null && activity.isFuture();
    }

    @Override
    public boolean canConfirm(Reservation reservation) {
        if (reservation.getStatut() == ReservationStatus.ANNULEE
                || reservation.getStatut() == ReservationStatus.TERMINEE) {
            return false;
        }
        Activity activity = activityService.findById(reservation.getActivityId()).orElse(null);
        return activity != null && activity.isFuture() && !activity.isCancelled();
    }

    private String reservationPath(int id) {
        return isAdmin() ? "/admin/reservations/" + id : "/parent/reservations/" + id;
    }

    private boolean isAdmin() {
        User currentUser = AppConfig.getInstance().getCurrentUser();
        return currentUser != null && currentUser.getRole() == UserRole.ADMIN;
    }
}
