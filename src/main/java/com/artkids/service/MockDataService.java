package com.artkids.service;

import com.artkids.enums.ActivityStatus;
import com.artkids.enums.ReservationStatus;
import com.artkids.enums.Sexe;
import com.artkids.enums.UserRole;
import com.artkids.model.Activity;
import com.artkids.model.Category;
import com.artkids.model.Child;
import com.artkids.model.Reservation;
import com.artkids.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class MockDataService {
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final ObservableList<Child> children = FXCollections.observableArrayList();
    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private final ObservableList<Activity> activities = FXCollections.observableArrayList();
    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    private final AtomicInteger userIdSequence = new AtomicInteger();
    private final AtomicInteger childIdSequence = new AtomicInteger();
    private final AtomicInteger categoryIdSequence = new AtomicInteger();
    private final AtomicInteger activityIdSequence = new AtomicInteger();
    private final AtomicInteger reservationIdSequence = new AtomicInteger();

    public void initialize() {
        users.clear();
        children.clear();
        categories.clear();
        activities.clear();
        reservations.clear();

        userIdSequence.set(0);
        childIdSequence.set(0);
        categoryIdSequence.set(0);
        activityIdSequence.set(0);
        reservationIdSequence.set(0);

        LocalDateTime now = LocalDateTime.now();

        User admin = new User(nextUserId(), "Ben Salah", "Admin", "admin@artkids.com", "admin123",
                "21 000 001", UserRole.ADMIN, true, now.minusMonths(3), now.minusDays(2));
        User parent = new User(nextUserId(), "Trabelsi", "Amel", "parent@artkids.com", "parent123",
                "21 000 002", UserRole.PARENT, true, now.minusMonths(2), now.minusDays(1));
        users.addAll(admin, parent);

        Child childOne = new Child(nextChildId(), "Trabelsi", "Lina", LocalDate.now().minusYears(8),
                Sexe.FILLE, parent.getId(), now.minusMonths(2), now.minusWeeks(2));
        Child childTwo = new Child(nextChildId(), "Trabelsi", "Youssef", LocalDate.now().minusYears(12),
                Sexe.GARCON, parent.getId(), now.minusMonths(2), now.minusWeeks(1));
        children.addAll(childOne, childTwo);

        Category drawing = createCategory("Dessin", "Ateliers d'expression graphique et croquis.", "dessin.png");
        Category painting = createCategory("Peinture", "Initiation aux couleurs et aux techniques de peinture.", "peinture.png");
        Category music = createCategory("Musique", "Decouverte des rythmes, percussions et melodies.", "musique.png");
        Category theatre = createCategory("Theatre", "Jeux de scene et confiance en soi.", "theatre.png");
        Category dance = createCategory("Danse", "Ateliers choregraphiques ludiques.", "danse.png");
        Category sculpture = createCategory("Sculpture", "Modelage et creation en volume.", "sculpture.png");
        categories.addAll(drawing, painting, music, theatre, dance, sculpture);

        activities.addAll(
                createActivity("Atelier aquarelle", "Une seance douce pour explorer les couleurs et l'eau.",
                        "activity-aquarelle.png", LocalDate.now().plusDays(3), LocalTime.of(10, 0),
                        LocalTime.of(11, 30), 10, 6, 10, new BigDecimal("35.00"),
                        ActivityStatus.OUVERTE, "Studio Lumiere", painting.getId()),
                createActivity("Percussions creatrices", "Decouverte du rythme avec instruments adaptes.",
                        "activity-percussions.png", LocalDate.now().plusDays(5), LocalTime.of(14, 0),
                        LocalTime.of(15, 30), 8, 10, 14, new BigDecimal("42.00"),
                        ActivityStatus.OUVERTE, "Salle Harmonie", music.getId()),
                createActivity("Mini ballet", "Atelier d'initiation a la danse classique pour les plus jeunes.",
                        "activity-ballet.png", LocalDate.now().plusDays(9), LocalTime.of(9, 30),
                        LocalTime.of(10, 45), 12, 4, 7, new BigDecimal("29.00"),
                        ActivityStatus.OUVERTE, "Salle Mouvements", dance.getId()),
                createActivity("Scene improvisee", "Une activite theatre tres demandee deja complete.",
                        "activity-theatre.png", LocalDate.now().plusDays(4), LocalTime.of(16, 0),
                        LocalTime.of(17, 30), 1, 11, 14, new BigDecimal("30.00"),
                        ActivityStatus.COMPLETE, "Boite Noire", theatre.getId()),
                createActivity("Sculpture vacances", "Atelier exceptionnel annule pour maintenance de salle.",
                        "activity-sculpture.png", LocalDate.now().plusDays(7), LocalTime.of(11, 0),
                        LocalTime.of(12, 30), 6, 8, 13, new BigDecimal("38.00"),
                        ActivityStatus.ANNULEE, "Atelier Terre", sculpture.getId()),
                createActivity("Croquis archive", "Ancienne activite deja terminee, conservee pour historique.",
                        "activity-croquis.png", LocalDate.now().minusDays(5), LocalTime.of(13, 0),
                        LocalTime.of(14, 30), 10, 7, 12, new BigDecimal("25.00"),
                        ActivityStatus.TERMINEE, "Studio Archive", drawing.getId())
        );

        reservations.addAll(
                createReservation(childOne.getId(), 1, ReservationStatus.CONFIRMEE, now.minusDays(1)),
                createReservation(childTwo.getId(), 2, ReservationStatus.EN_ATTENTE, now.minusHours(10)),
                createReservation(childTwo.getId(), 4, ReservationStatus.CONFIRMEE, now.minusDays(3)),
                createReservation(childOne.getId(), 6, ReservationStatus.TERMINEE, now.minusDays(10))
        );
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public ObservableList<Child> getChildren() {
        return children;
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }

    public ObservableList<Activity> getActivities() {
        return activities;
    }

    public ObservableList<Reservation> getReservations() {
        return reservations;
    }

    public int nextUserId() {
        return userIdSequence.incrementAndGet();
    }

    public int nextChildId() {
        return childIdSequence.incrementAndGet();
    }

    public int nextCategoryId() {
        return categoryIdSequence.incrementAndGet();
    }

    public int nextActivityId() {
        return activityIdSequence.incrementAndGet();
    }

    public int nextReservationId() {
        return reservationIdSequence.incrementAndGet();
    }

    private Category createCategory(String nom, String description, String image) {
        LocalDateTime now = LocalDateTime.now();
        return new Category(nextCategoryId(), nom, description, image, now.minusMonths(1), now.minusDays(1));
    }

    private Activity createActivity(String titre, String description, String image, LocalDate dateActivite,
                                    LocalTime heureDebut, LocalTime heureFin, int capaciteMax, int ageMin,
                                    int ageMax, BigDecimal prix, ActivityStatus statut, String lieu, int categoryId) {
        LocalDateTime now = LocalDateTime.now();
        return new Activity(nextActivityId(), titre, description, image, dateActivite, heureDebut, heureFin,
                capaciteMax, ageMin, ageMax, prix, statut, lieu, categoryId, now.minusWeeks(2), now.minusDays(1));
    }

    private Reservation createReservation(int childId, int activityId, ReservationStatus statut,
                                          LocalDateTime reservationDate) {
        return new Reservation(nextReservationId(), reservationDate, statut, childId, activityId,
                reservationDate, reservationDate);
    }
}
