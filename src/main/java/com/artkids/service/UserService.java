package com.artkids.service;

import com.artkids.config.AppConfig;
import com.artkids.enums.UserRole;
import com.artkids.model.User;
import com.artkids.util.ValidationUtils;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserService {
    private final MockDataService dataService;

    public UserService(MockDataService dataService) {
        this.dataService = dataService;
    }

    public ObservableList<User> findAll() {
        return dataService.getUsers();
    }

    public Optional<User> findById(int id) {
        return dataService.getUsers().stream().filter(user -> user.getId() == id).findFirst();
    }

    public boolean emailExists(String email) {
        if (ValidationUtils.isBlank(email)) {
            return false;
        }
        String normalizedEmail = email.trim();
        return dataService.getUsers().stream()
                .anyMatch(existing -> existing.getEmail().equalsIgnoreCase(normalizedEmail));
    }

    public User create(User user) {
        validate(user, true);
        LocalDateTime now = LocalDateTime.now();
        user.setId(dataService.nextUserId());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        dataService.getUsers().add(user);
        return user;
    }

    public User createParentAccount(String nom, String prenom, String email, String telephone, String password) {
        User user = new User();
        user.setNom(nom == null ? null : nom.trim());
        user.setPrenom(prenom == null ? null : prenom.trim());
        user.setEmail(email == null ? null : email.trim());
        user.setTelephone(ValidationUtils.isBlank(telephone) ? "" : telephone.trim());
        user.setPassword(password);
        user.setRole(UserRole.PARENT);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return create(user);
    }

    public User update(User user) {
        User existing = findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
        validate(user, false);
        User currentUser = requireCurrentUser();
        if (existing.getId() == currentUser.getId() && user.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Vous ne pouvez pas retirer votre propre role admin.");
        }
        if (existing.getId() == currentUser.getId() && !user.isActive()) {
            throw new IllegalArgumentException("Vous ne pouvez pas vous desactiver vous-meme.");
        }
        if (existing.getRole() == UserRole.ADMIN && user.getRole() != UserRole.ADMIN && countAdmins() <= 1) {
            throw new IllegalArgumentException("Le dernier administrateur ne peut pas perdre son role admin.");
        }
        if (existing.getRole() == UserRole.ADMIN && existing.isActive() && !user.isActive() && countActiveAdmins() <= 1) {
            throw new IllegalArgumentException("Le dernier administrateur actif ne peut pas etre desactive.");
        }

        if (ValidationUtils.isBlank(user.getPassword())) {
            user.setPassword(existing.getPassword());
        }
        user.setCreatedAt(existing.getCreatedAt());
        user.setUpdatedAt(LocalDateTime.now());
        int index = dataService.getUsers().indexOf(existing);
        dataService.getUsers().set(index, user);

        if (currentUser.getId() == user.getId()) {
            AppConfig.getInstance().setCurrentUser(user);
        }
        return user;
    }

    public User toggleStatus(int userId) {
        User existing = findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
        User currentUser = requireCurrentUser();
        if (existing.getId() == currentUser.getId()) {
            throw new IllegalArgumentException("Vous ne pouvez pas modifier le statut de votre propre compte.");
        }
        if (existing.getRole() == UserRole.ADMIN && existing.isActive() && countActiveAdmins() <= 1) {
            throw new IllegalArgumentException("Le dernier administrateur actif ne peut pas etre desactive.");
        }

        existing.setActive(!existing.isActive());
        existing.setUpdatedAt(LocalDateTime.now());
        replace(existing);
        return existing;
    }

    public boolean delete(int userId) {
        User existing = findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
        User currentUser = requireCurrentUser();
        if (existing.getId() == currentUser.getId()) {
            throw new IllegalArgumentException("Vous ne pouvez pas supprimer votre propre compte.");
        }
        if (existing.getRole() == UserRole.ADMIN && countAdmins() <= 1) {
            throw new IllegalArgumentException("Le dernier administrateur ne peut pas etre supprime.");
        }

        boolean hasChildren = dataService.getChildren().stream().anyMatch(child -> child.getParentId() == existing.getId());
        if (hasChildren) {
            if (existing.getRole() == UserRole.ADMIN && countActiveAdmins() <= 1) {
                throw new IllegalArgumentException("Le dernier administrateur actif ne peut pas etre desactive.");
            }
            existing.setActive(false);
            existing.setUpdatedAt(LocalDateTime.now());
            replace(existing);
            return false;
        }

        dataService.getUsers().remove(existing);
        return true;
    }

    public long countParents() {
        return dataService.getUsers().stream().filter(User::isParent).count();
    }

    public long countAdmins() {
        return dataService.getUsers().stream().filter(User::isAdmin).count();
    }

    public long countActiveAdmins() {
        return dataService.getUsers().stream().filter(User::isAdmin).filter(User::isActive).count();
    }

    private void validate(User user, boolean creating) {
        ValidationUtils.validateRequired(user.getNom(), "Nom");
        ValidationUtils.validateRequired(user.getPrenom(), "Prenom");
        ValidationUtils.validateRequired(user.getEmail(), "Email");
        ValidationUtils.validateRequired(user.getRole(), "Role");
        if (!ValidationUtils.isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("L'adresse email est invalide.");
        }
        if (creating) {
            ValidationUtils.validateRequired(user.getPassword(), "Mot de passe");
        }
        if (creating && user.getPassword().trim().length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caracteres.");
        }
        boolean duplicate = dataService.getUsers().stream()
                .anyMatch(existing -> existing.getId() != user.getId()
                        && existing.getEmail().equalsIgnoreCase(user.getEmail().trim()));
        if (duplicate) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe deja.");
        }
    }

    private void replace(User user) {
        User existing = findById(user.getId()).orElseThrow();
        int index = dataService.getUsers().indexOf(existing);
        dataService.getUsers().set(index, user);
    }

    private User requireCurrentUser() {
        User currentUser = AppConfig.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("Aucun utilisateur connecte.");
        }
        return currentUser;
    }
}
