package com.artkids.service;

import com.artkids.config.AppConfig;
import com.artkids.enums.UserRole;
import com.artkids.model.Child;
import com.artkids.model.User;
import com.artkids.util.JsonUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

public class ChildApiService extends ChildService {
    protected final ApiClient apiClient;

    public ChildApiService(ApiClient apiClient, MockDataService dataService) {
        super(dataService);
        this.apiClient = apiClient;
    }

    @Override
    public ObservableList<Child> findAll() {
        return FXCollections.observableArrayList(ApiMappers.array(apiClient.get("/admin/children")).stream()
                .map(ApiMappers::child)
                .toList());
    }

    @Override
    public ObservableList<Child> findByParent(int parentId) {
        User currentUser = AppConfig.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getRole() == UserRole.ADMIN) {
            return FXCollections.observableArrayList(findAll().stream()
                    .filter(child -> child.getParentId() == parentId)
                    .toList());
        }
        return FXCollections.observableArrayList(ApiMappers.array(apiClient.get("/parent/children")).stream()
                .map(ApiMappers::child)
                .toList());
    }

    @Override
    public Optional<Child> findById(int id) {
        try {
            return Optional.of(ApiMappers.child(JsonUtils.parseObject(apiClient.get(childPath(id)))));
        } catch (ApiException exception) {
            if (exception.getStatusCode() == 404) {
                return Optional.empty();
            }
            throw exception;
        }
    }

    @Override
    public Child create(Child child) {
        String response = apiClient.post("/parent/children", JsonUtils.stringify(ApiMappers.childPayload(child)));
        return ApiMappers.child(JsonUtils.parseObject(response));
    }

    @Override
    public Child update(Child child) {
        String response = apiClient.put("/parent/children/" + child.getId(),
                JsonUtils.stringify(ApiMappers.childPayload(child)));
        return ApiMappers.child(JsonUtils.parseObject(response));
    }

    @Override
    public void delete(int id) {
        apiClient.delete("/parent/children/" + id);
    }

    private String childPath(int id) {
        User currentUser = AppConfig.getInstance().getCurrentUser();
        return currentUser != null && currentUser.getRole() == UserRole.ADMIN
                ? "/admin/children/" + id
                : "/parent/children/" + id;
    }
}
