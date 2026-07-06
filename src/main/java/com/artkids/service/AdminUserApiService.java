package com.artkids.service;

import com.artkids.config.AppConfig;
import com.artkids.enums.UserRole;
import com.artkids.model.User;
import com.artkids.util.JsonUtils;
import com.artkids.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

public class AdminUserApiService extends UserService {
    private final ApiClient apiClient;

    public AdminUserApiService(ApiClient apiClient, MockDataService dataService) {
        super(dataService);
        this.apiClient = apiClient;
    }

    @Override
    public ObservableList<User> findAll() {
        return FXCollections.observableArrayList(ApiMappers.array(apiClient.get("/admin/users")).stream()
                .map(ApiMappers::user)
                .toList());
    }

    @Override
    public Optional<User> findById(int id) {
        try {
            return Optional.of(ApiMappers.user(JsonUtils.parseObject(apiClient.get("/admin/users/" + id))));
        } catch (ApiException exception) {
            if (exception.getStatusCode() == 404) {
                return Optional.empty();
            }
            throw exception;
        }
    }

    @Override
    public boolean emailExists(String email) {
        if (ValidationUtils.isBlank(email) || AppConfig.getInstance().getCurrentUser() == null) {
            return false;
        }
        return findAll().stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email.trim()));
    }

    @Override
    public User create(User user) {
        String response = apiClient.post("/admin/users", JsonUtils.stringify(ApiMappers.userPayload(user)));
        return ApiMappers.user(JsonUtils.parseObject(response));
    }

    @Override
    public User createParentAccount(String nom, String prenom, String email, String telephone, String password) {
        String response = apiClient.post("/register",
                JsonUtils.stringify(ApiMappers.registerPayload(nom, prenom, email, telephone, password)));
        User user = ApiMappers.user(JsonUtils.parseObject(response));
        user.setRole(UserRole.PARENT);
        user.setActive(true);
        return user;
    }

    @Override
    public User update(User user) {
        String response = apiClient.put("/admin/users/" + user.getId(),
                JsonUtils.stringify(ApiMappers.userPayload(user)));
        User updated = ApiMappers.user(JsonUtils.parseObject(response));
        if (AppConfig.getInstance().getCurrentUser() != null
                && AppConfig.getInstance().getCurrentUser().getId() == updated.getId()) {
            AppConfig.getInstance().setCurrentUser(updated);
        }
        return updated;
    }

    @Override
    public User toggleStatus(int userId) {
        User user = findById(userId).orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
        user.setActive(!user.isActive());
        return update(user);
    }

    @Override
    public boolean delete(int userId) {
        apiClient.delete("/admin/users/" + userId);
        return true;
    }

    @Override
    public long countParents() {
        return findAll().stream().filter(User::isParent).count();
    }

    @Override
    public long countAdmins() {
        return findAll().stream().filter(User::isAdmin).count();
    }

    @Override
    public long countActiveAdmins() {
        return findAll().stream().filter(User::isAdmin).filter(User::isActive).count();
    }
}
