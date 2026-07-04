package com.artkids.controller.backoffice;

import com.artkids.config.AppConfig;
import com.artkids.model.Category;
import com.artkids.service.CategoryService;
import com.artkids.util.AlertUtils;
import com.artkids.util.DataReceiver;
import com.artkids.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AdminCategoryFormController implements DataReceiver<Category> {
    private final CategoryService categoryService = AppConfig.getInstance().getCategoryService();
    private Category editingCategory;

    @FXML
    private Label pageTitleLabel;
    @FXML
    private TextField nomField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField imageField;

    @FXML
    public void initialize() {
        updateTitle();
    }

    @Override
    public void setData(Category category) {
        this.editingCategory = category;
        if (category != null) {
            nomField.setText(category.getNom());
            descriptionArea.setText(category.getDescription());
            imageField.setText(category.getImage());
        }
        updateTitle();
    }

    @FXML
    private void saveCategory() {
        try {
            Category category = new Category();
            if (editingCategory != null) {
                category.setId(editingCategory.getId());
            }
            category.setNom(nomField.getText());
            category.setDescription(descriptionArea.getText());
            category.setImage(imageField.getText());

            if (editingCategory == null) {
                categoryService.create(category);
                AlertUtils.showSuccess("Categorie ajoutee avec succes.");
            } else {
                categoryService.update(category);
                AlertUtils.showSuccess("Categorie mise a jour avec succes.");
            }
            SceneManager.getInstance().showAdminCategories();
        } catch (IllegalArgumentException exception) {
            AlertUtils.showError(exception.getMessage());
        }
    }

    @FXML
    private void cancel() {
        SceneManager.getInstance().showAdminCategories();
    }

    private void updateTitle() {
        if (pageTitleLabel != null) {
            pageTitleLabel.setText(editingCategory == null ? "Ajouter une categorie" : "Modifier une categorie");
        }
    }
}
