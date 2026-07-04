package com.artkids.service;

import com.artkids.config.AppConfig;
import com.artkids.enums.UserRole;
import com.artkids.model.Child;
import com.artkids.model.User;
import com.artkids.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class ChildService {
    private final MockDataService dataService;

    public ChildService(MockDataService dataService) {
        this.dataService = dataService;
    }

    public ObservableList<Child> findAll() {
        return dataService.getChildren();
    }

    public ObservableList<Child> findByParent(int parentId) {
        User currentUser = requireCurrentUser();
        if (currentUser.getRole() == UserRole.PARENT && currentUser.getId() != parentId) {
            throw new IllegalArgumentException("Vous ne pouvez consulter que vos propres enfants.");
        }
        return FXCollections.observableArrayList(
                dataService.getChildren().stream()
                        .filter(child -> child.getParentId() == parentId)
                        .toList()
        );
    }

    public Optional<Child> findById(int id) {
        return dataService.getChildren().stream().filter(child -> child.getId() == id).findFirst();
    }

    public Child create(Child child) {
        validate(child);
        User currentUser = requireCurrentUser();
        if (currentUser.getRole() == UserRole.PARENT) {
            child.setParentId(currentUser.getId());
        }
        child.setId(dataService.nextChildId());
        LocalDateTime now = LocalDateTime.now();
        child.setCreatedAt(now);
        child.setUpdatedAt(now);
        dataService.getChildren().add(child);
        return child;
    }

    public Child update(Child child) {
        validate(child);
        Child existing = findById(child.getId())
                .orElseThrow(() -> new IllegalArgumentException("Enfant introuvable."));
        assertCanManage(existing);
        child.setParentId(existing.getParentId());
        child.setCreatedAt(existing.getCreatedAt());
        child.setUpdatedAt(LocalDateTime.now());
        int index = dataService.getChildren().indexOf(existing);
        dataService.getChildren().set(index, child);
        return child;
    }

    public void delete(int id) {
        Child child = findById(id).orElseThrow(() -> new IllegalArgumentException("Enfant introuvable."));
        assertCanManage(child);
        dataService.getReservations().removeIf(reservation -> reservation.getChildId() == id);
        dataService.getChildren().remove(child);
        AppConfig.getInstance().getActivityService().refreshStatuses();
    }

    private void validate(Child child) {
        ValidationUtils.validateRequired(child.getNom(), "Nom");
        ValidationUtils.validateRequired(child.getPrenom(), "Prenom");
        ValidationUtils.validateRequired(child.getDateNaissance(), "Date de naissance");
        ValidationUtils.validateRequired(child.getSexe(), "Sexe");
        if (child.getDateNaissance().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date de naissance ne peut pas etre future.");
        }
    }

    private void assertCanManage(Child child) {
        User currentUser = requireCurrentUser();
        if (currentUser.getRole() == UserRole.PARENT && child.getParentId() != currentUser.getId()) {
            throw new IllegalArgumentException("Vous ne pouvez modifier que vos propres enfants.");
        }
    }

    private User requireCurrentUser() {
        User currentUser = AppConfig.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("Aucun utilisateur connecte.");
        }
        return currentUser;
    }
}
