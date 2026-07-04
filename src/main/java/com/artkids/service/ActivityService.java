package com.artkids.service;

import com.artkids.enums.ActivityStatus;
import com.artkids.enums.ReservationStatus;
import com.artkids.model.Activity;
import com.artkids.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class ActivityService {
    private final MockDataService dataService;

    public ActivityService(MockDataService dataService) {
        this.dataService = dataService;
    }

    public ObservableList<Activity> findAll() {
        refreshStatuses();
        return dataService.getActivities();
    }

    public ObservableList<Activity> findOpenActivities() {
        refreshStatuses();
        return FXCollections.observableArrayList(
                dataService.getActivities().stream()
                        .filter(activity -> activity.getStatut() == ActivityStatus.OUVERTE)
                        .filter(Activity::isFuture)
                        .toList()
        );
    }

    public Optional<Activity> findById(int id) {
        refreshStatuses();
        return findByIdInternal(id);
    }

    public Activity create(Activity activity) {
        validate(activity);
        LocalDateTime now = LocalDateTime.now();
        activity.setId(dataService.nextActivityId());
        activity.setCreatedAt(now);
        activity.setUpdatedAt(now);
        dataService.getActivities().add(activity);
        refreshStatuses();
        return activity;
    }

    public Activity update(Activity activity) {
        Activity existing = findById(activity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Activite introuvable."));
        validate(activity);
        activity.setCreatedAt(existing.getCreatedAt());
        activity.setUpdatedAt(LocalDateTime.now());
        int index = dataService.getActivities().indexOf(existing);
        dataService.getActivities().set(index, activity);
        refreshStatuses();
        return activity;
    }

    public void delete(int id) {
        Activity existing = findById(id).orElseThrow(() -> new IllegalArgumentException("Activite introuvable."));
        boolean hasActiveReservations = dataService.getReservations().stream()
                .anyMatch(reservation -> reservation.getActivityId() == id
                        && reservation.getStatut() != ReservationStatus.ANNULEE);
        if (hasActiveReservations) {
            throw new IllegalArgumentException("Impossible de supprimer une activite avec des reservations actives.");
        }
        dataService.getActivities().remove(existing);
    }

    public int countReservationsForActivity(int activityId) {
        return (int) dataService.getReservations().stream()
                .filter(reservation -> reservation.getActivityId() == activityId)
                .filter(reservation -> reservation.getStatut() != ReservationStatus.ANNULEE)
                .count();
    }

    public int getAvailablePlaces(int activityId) {
        Activity activity = findByIdInternal(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activite introuvable."));
        return Math.max(0, activity.getCapaciteMax() - countReservationsForActivity(activityId));
    }

    public void refreshStatuses() {
        for (Activity activity : dataService.getActivities()) {
            syncStatus(activity);
        }
    }

    private void syncStatus(Activity activity) {
        if (activity.getDateActivite() != null && !activity.isFuture()) {
            activity.setStatut(ActivityStatus.TERMINEE);
            return;
        }
        if (activity.getStatut() == ActivityStatus.ANNULEE) {
            return;
        }
        if (getAvailablePlaces(activity.getId()) <= 0) {
            activity.setStatut(ActivityStatus.COMPLETE);
        } else if (activity.getStatut() != ActivityStatus.TERMINEE) {
            activity.setStatut(ActivityStatus.OUVERTE);
        }
    }

    private void validate(Activity activity) {
        ValidationUtils.validateRequired(activity.getTitre(), "Titre");
        ValidationUtils.validateRequired(activity.getDescription(), "Description");
        ValidationUtils.validateRequired(activity.getDateActivite(), "Date de l'activite");
        ValidationUtils.validateRequired(activity.getHeureDebut(), "Heure de debut");
        ValidationUtils.validateRequired(activity.getHeureFin(), "Heure de fin");
        ValidationUtils.validateRequired(activity.getStatut(), "Statut");

        if (activity.getCategoryId() <= 0) {
            throw new IllegalArgumentException("La categorie est obligatoire.");
        }
        if (!activity.getDateActivite().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date de l'activite doit etre future.");
        }
        if (!activity.getHeureFin().isAfter(activity.getHeureDebut())) {
            throw new IllegalArgumentException("L'heure de fin doit etre superieure a l'heure de debut.");
        }
        if (activity.getCapaciteMax() <= 0) {
            throw new IllegalArgumentException("La capacite maximale doit etre positive.");
        }
        if (activity.getAgeMin() < 0) {
            throw new IllegalArgumentException("L'age minimum doit etre positif.");
        }
        if (activity.getAgeMax() < activity.getAgeMin()) {
            throw new IllegalArgumentException("L'age maximum doit etre superieur ou egal a l'age minimum.");
        }

        BigDecimal price = activity.getPrix() == null ? BigDecimal.ZERO : activity.getPrix();
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le prix doit etre positif ou nul.");
        }
        activity.setPrix(price);
    }

    private Optional<Activity> findByIdInternal(int id) {
        return dataService.getActivities().stream().filter(activity -> activity.getId() == id).findFirst();
    }
}
