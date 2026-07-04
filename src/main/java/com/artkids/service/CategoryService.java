package com.artkids.service;

import com.artkids.model.Category;
import com.artkids.util.ValidationUtils;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.Optional;

public class CategoryService {
    private final MockDataService dataService;

    public CategoryService(MockDataService dataService) {
        this.dataService = dataService;
    }

    public ObservableList<Category> findAll() {
        return dataService.getCategories();
    }

    public Optional<Category> findById(int id) {
        return dataService.getCategories().stream().filter(category -> category.getId() == id).findFirst();
    }

    public Category create(Category category) {
        validate(category, -1);
        LocalDateTime now = LocalDateTime.now();
        category.setId(dataService.nextCategoryId());
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
        dataService.getCategories().add(category);
        return category;
    }

    public Category update(Category category) {
        Category existing = findById(category.getId())
                .orElseThrow(() -> new IllegalArgumentException("Categorie introuvable."));
        validate(category, existing.getId());
        category.setCreatedAt(existing.getCreatedAt());
        category.setUpdatedAt(LocalDateTime.now());
        int index = dataService.getCategories().indexOf(existing);
        dataService.getCategories().set(index, category);
        return category;
    }

    public void delete(int id) {
        Category existing = findById(id).orElseThrow(() -> new IllegalArgumentException("Categorie introuvable."));
        boolean usedByActivity = dataService.getActivities().stream()
                .anyMatch(activity -> activity.getCategoryId() == id);
        if (usedByActivity) {
            throw new IllegalArgumentException("Cette categorie est utilisee par au moins une activite.");
        }
        dataService.getCategories().remove(existing);
    }

    private void validate(Category category, int currentId) {
        ValidationUtils.validateRequired(category.getNom(), "Nom");
        boolean duplicate = dataService.getCategories().stream()
                .anyMatch(existing -> existing.getId() != currentId
                        && existing.getNom().equalsIgnoreCase(category.getNom().trim()));
        if (duplicate) {
            throw new IllegalArgumentException("Une categorie avec ce nom existe deja.");
        }
    }
}
