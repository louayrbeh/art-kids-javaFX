package com.artkids.service;

import com.artkids.config.AppConfig;
import com.artkids.enums.ActivityStatus;
import com.artkids.enums.ReservationStatus;
import com.artkids.enums.UserRole;
import com.artkids.model.Activity;
import com.artkids.model.Child;
import com.artkids.model.Reservation;
import com.artkids.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public class ReservationService {
    private static final Set<ReservationStatus> ACTIVE_STATUSES =
            Set.of(ReservationStatus.EN_ATTENTE, ReservationStatus.CONFIRMEE, ReservationStatus.TERMINEE);

    private final MockDataService dataService;
    private final ChildService childService;
    private final ActivityService activityService;

    public ReservationService(MockDataService dataService, ChildService childService, ActivityService activityService) {
        this.dataService = dataService;
        this.childService = childService;
        this.activityService = activityService;
    }

    public ObservableList<Reservation> findAll() {
        activityService.refreshStatuses();
        return dataService.getReservations();
    }

    public Optional<Reservation> findById(int id) {
        return dataService.getReservations().stream().filter(reservation -> reservation.getId() == id).findFirst();
    }

    public ObservableList<Reservation> findByParent(int parentId) {
        User currentUser = requireCurrentUser();
        if (currentUser.getRole() == UserRole.PARENT && currentUser.getId() != parentId) {
            throw new IllegalArgumentException("Vous ne pouvez voir que vos propres reservations.");
        }
        return FXCollections.observableArrayList(
                dataService.getReservations().stream()
                        .filter(reservation -> childService.findById(reservation.getChildId())
                                .map(child -> child.getParentId() == parentId)
                                .orElse(false))
                        .toList()
        );
    }

    public ObservableList<Reservation> findByActivity(int activityId) {
        return FXCollections.observableArrayList(
                dataService.getReservations().stream()
                        .filter(reservation -> reservation.getActivityId() == activityId)
                        .toList()
        );
    }

    public Reservation createReservation(int childId, int activityId) {
        Child child = childService.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("Enfant introuvable."));
        Activity activity = activityService.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activite introuvable."));
        assertParentOwnsChild(child);
        assertReservationAllowed(child, activity);

        Reservation reservation = new Reservation();
        reservation.setId(dataService.nextReservationId());
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut(ReservationStatus.EN_ATTENTE);
        reservation.setChildId(childId);
        reservation.setActivityId(activityId);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());
        dataService.getReservations().add(reservation);
        activityService.refreshStatuses();
        return reservation;
    }

    public Reservation cancelReservation(int reservationId) {
        Reservation reservation = findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation introuvable."));
        Child child = childService.findById(reservation.getChildId())
                .orElseThrow(() -> new IllegalArgumentException("Enfant introuvable."));
        Activity activity = activityService.findById(reservation.getActivityId())
                .orElseThrow(() -> new IllegalArgumentException("Activite introuvable."));
        assertParentOwnsReservationIfNeeded(child);

        if (reservation.getStatut() == ReservationStatus.TERMINEE) {
            throw new IllegalArgumentException("Une reservation terminee ne peut plus etre annulee.");
        }
        if (reservation.getStatut() == ReservationStatus.ANNULEE) {
            throw new IllegalArgumentException("Cette reservation est deja annulee.");
        }
        if (requireCurrentUser().getRole() == UserRole.PARENT && !activity.isFuture()) {
            throw new IllegalArgumentException("Une reservation pour une activite passee ne peut plus etre annulee.");
        }

        reservation.setStatut(ReservationStatus.ANNULEE);
        reservation.setUpdatedAt(LocalDateTime.now());
        replaceReservation(reservation);
        activityService.refreshStatuses();
        return reservation;
    }

    public Reservation confirmReservation(int reservationId) {
        Reservation reservation = findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation introuvable."));
        Activity activity = activityService.findById(reservation.getActivityId())
                .orElseThrow(() -> new IllegalArgumentException("Activite introuvable."));
        if (reservation.getStatut() == ReservationStatus.ANNULEE) {
            throw new IllegalArgumentException("Une reservation annulee ne peut pas etre confirmee.");
        }
        if (reservation.getStatut() == ReservationStatus.TERMINEE) {
            throw new IllegalArgumentException("Une reservation terminee ne peut pas etre confirmee.");
        }
        if (!activity.isFuture() || activity.getStatut() == ActivityStatus.ANNULEE
                || activity.getStatut() == ActivityStatus.TERMINEE) {
            throw new IllegalArgumentException("Cette activite ne peut plus accepter de confirmation.");
        }
        if (activityService.countReservationsForActivity(activity.getId()) > activity.getCapaciteMax()) {
            throw new IllegalArgumentException("La capacite maximale de l'activite est deja depassee.");
        }

        reservation.setStatut(ReservationStatus.CONFIRMEE);
        reservation.setUpdatedAt(LocalDateTime.now());
        replaceReservation(reservation);
        activityService.refreshStatuses();
        return reservation;
    }

    public boolean canCancel(Reservation reservation) {
        Activity activity = activityService.findById(reservation.getActivityId()).orElse(null);
        if (activity == null || reservation.getStatut() == ReservationStatus.ANNULEE
                || reservation.getStatut() == ReservationStatus.TERMINEE) {
            return false;
        }
        return requireCurrentUser().getRole() == UserRole.ADMIN || activity.isFuture();
    }

    public boolean canConfirm(Reservation reservation) {
        Activity activity = activityService.findById(reservation.getActivityId()).orElse(null);
        return activity != null
                && reservation.getStatut() != ReservationStatus.ANNULEE
                && reservation.getStatut() != ReservationStatus.TERMINEE
                && activity.isFuture()
                && activity.getStatut() != ActivityStatus.ANNULEE
                && activity.getStatut() != ActivityStatus.TERMINEE
                && activityService.countReservationsForActivity(activity.getId()) <= activity.getCapaciteMax();
    }

    private void assertReservationAllowed(Child child, Activity activity) {
        boolean alreadyReserved = dataService.getReservations().stream()
                .anyMatch(existing -> existing.getChildId() == child.getId()
                        && existing.getActivityId() == activity.getId());
        if (alreadyReserved) {
            throw new IllegalArgumentException("Cet enfant est deja inscrit a cette activite.");
        }
        if (activity.getStatut() == ActivityStatus.COMPLETE || activityService.getAvailablePlaces(activity.getId()) <= 0) {
            throw new IllegalArgumentException("Cette activite est complete.");
        }
        if (activity.getStatut() == ActivityStatus.ANNULEE) {
            throw new IllegalArgumentException("Cette activite est annulee.");
        }
        if (activity.getStatut() == ActivityStatus.TERMINEE || !activity.isFuture()) {
            throw new IllegalArgumentException("Cette activite est deja terminee ou passee.");
        }
        if (child.getAge() < activity.getAgeMin() || child.getAge() > activity.getAgeMax()) {
            throw new IllegalArgumentException("L'age de l'enfant n'est pas compatible avec cette activite.");
        }
        if (activityService.countReservationsForActivity(activity.getId()) >= activity.getCapaciteMax()) {
            throw new IllegalArgumentException("Il n'y a plus de places disponibles pour cette activite.");
        }
    }

    private void assertParentOwnsChild(Child child) {
        User currentUser = requireCurrentUser();
        if (currentUser.getRole() == UserRole.PARENT && child.getParentId() != currentUser.getId()) {
            throw new IllegalArgumentException("Vous ne pouvez reserver que pour vos propres enfants.");
        }
    }

    private void assertParentOwnsReservationIfNeeded(Child child) {
        User currentUser = requireCurrentUser();
        if (currentUser.getRole() == UserRole.PARENT && child.getParentId() != currentUser.getId()) {
            throw new IllegalArgumentException("Vous ne pouvez gerer que vos propres reservations.");
        }
    }

    private void replaceReservation(Reservation reservation) {
        Reservation existing = findById(reservation.getId()).orElseThrow();
        int index = dataService.getReservations().indexOf(existing);
        dataService.getReservations().set(index, reservation);
    }

    private User requireCurrentUser() {
        User currentUser = AppConfig.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("Aucun utilisateur connecte.");
        }
        return currentUser;
    }
}
