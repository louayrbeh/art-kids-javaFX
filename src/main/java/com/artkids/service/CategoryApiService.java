package com.artkids.service;

import com.artkids.config.AppConfig;
import com.artkids.enums.UserRole;
import com.artkids.model.Category;
import com.artkids.model.User;
import com.artkids.util.JsonUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryApiService extends CategoryService {
    protected final ApiClient apiClient;

    public CategoryApiService(ApiClient apiClient, MockDataService dataService) {
        super(dataService);
        this.apiClient = apiClient;
    }

    @Override
    public ObservableList<Category> findAll() {
        User currentUser = AppConfig.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getRole() == UserRole.ADMIN) {
            return FXCollections.observableArrayList(ApiMappers.array(apiClient.get("/admin/categories")).stream()
                    .map(ApiMappers::category)
                    .toList());
        }
        return FXCollections.observableArrayList(ApiMappers.array(apiClient.get("/parent/activities")).stream()
                .map(ApiMappers::object)
                .map(activity -> ApiMappers.category(activity.get("category")))
                .filter(category -> category.getId() > 0)
                .collect(Collectors.toMap(
                        Category::getId,
                        category -> category,
                        (left, right) -> left,
                        LinkedHashMap::new
                ))
                .values());
    }

    @Override
    public Optional<Category> findById(int id) {
        try {
            return Optional.of(ApiMappers.category(JsonUtils.parseObject(apiClient.get("/admin/categories/" + id))));
        } catch (ApiException exception) {
            if (exception.getStatusCode() == 404 || exception.getStatusCode() == 403) {
                return findAll().stream().filter(category -> category.getId() == id).findFirst();
            }
            throw exception;
        }
    }

    @Override
    public Category create(Category category) {
        String response = apiClient.post("/admin/categories", JsonUtils.stringify(ApiMappers.categoryPayload(category)));
        return ApiMappers.category(JsonUtils.parseObject(response));
    }

    @Override
    public Category update(Category category) {
        String response = apiClient.put("/admin/categories/" + category.getId(),
                JsonUtils.stringify(ApiMappers.categoryPayload(category)));
        return ApiMappers.category(JsonUtils.parseObject(response));
    }

    @Override
    public void delete(int id) {
        apiClient.delete("/admin/categories/" + id);
    }
}
