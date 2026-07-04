package com.artkids.util;

import com.artkids.config.AppConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public final class SceneManager {
    private static final SceneManager INSTANCE = new SceneManager();

    private Stage primaryStage;

    private SceneManager() {
    }

    public static SceneManager getInstance() {
        return INSTANCE;
    }

    public void initialize(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showLogin() {
        AppConfig.getInstance().clearSession();
        switchScene("/com/artkids/view/auth/login.fxml", "ArtKids - Connexion", null);
    }

    public void showParentDashboard() {
        switchScene("/com/artkids/view/frontoffice/parent_dashboard.fxml",
                "ArtKids - Espace Parent", null);
    }

    public void showParentChildren() {
        switchScene("/com/artkids/view/frontoffice/parent_children.fxml",
                "ArtKids - Mes enfants", null);
    }

    public void showParentChildForm(Object child) {
        switchScene("/com/artkids/view/frontoffice/parent_child_form.fxml",
                "ArtKids - Fiche enfant", child);
    }

    public void showParentActivities() {
        switchScene("/com/artkids/view/frontoffice/parent_activities.fxml",
                "ArtKids - Activites", null);
    }

    public void showParentActivityDetails(Object activity) {
        switchScene("/com/artkids/view/frontoffice/parent_activity_details.fxml",
                "ArtKids - Detail activite", activity);
    }

    public void showParentReservations() {
        switchScene("/com/artkids/view/frontoffice/parent_reservations.fxml",
                "ArtKids - Mes reservations", null);
    }

    public void showAdminDashboard() {
        switchScene("/com/artkids/view/backoffice/admin_dashboard.fxml",
                "ArtKids - Back-Office", null);
    }

    public void showAdminCategories() {
        switchScene("/com/artkids/view/backoffice/admin_categories.fxml",
                "ArtKids - Categories", null);
    }

    public void showAdminCategoryForm(Object category) {
        switchScene("/com/artkids/view/backoffice/admin_category_form.fxml",
                "ArtKids - Fiche categorie", category);
    }

    public void showAdminActivities() {
        switchScene("/com/artkids/view/backoffice/admin_activities.fxml",
                "ArtKids - Activites", null);
    }

    public void showAdminActivityForm(Object activity) {
        switchScene("/com/artkids/view/backoffice/admin_activity_form.fxml",
                "ArtKids - Fiche activite", activity);
    }

    public void showAdminUsers() {
        switchScene("/com/artkids/view/backoffice/admin_users.fxml",
                "ArtKids - Utilisateurs", null);
    }

    public void showAdminUserForm(Object user) {
        switchScene("/com/artkids/view/backoffice/admin_user_form.fxml",
                "ArtKids - Fiche utilisateur", user);
    }

    public void showAdminChildren() {
        switchScene("/com/artkids/view/backoffice/admin_children.fxml",
                "ArtKids - Enfants", null);
    }

    public void showAdminReservations() {
        switchScene("/com/artkids/view/backoffice/admin_reservations.fxml",
                "ArtKids - Reservations", null);
    }

    @SuppressWarnings("unchecked")
    public void switchScene(String resourcePath, String title, Object data) {
        try {
            System.out.println("Chargement FXML : " + resourcePath);
            URL resource = SceneManager.class.getResource(resourcePath);
            if (resource == null) {
                throw new IllegalStateException("Vue introuvable : " + resourcePath);
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            Object controller = loader.getController();
            if (data != null && controller instanceof DataReceiver<?> receiver) {
                ((DataReceiver<Object>) receiver).setData(data);
            }

            Scene scene = new Scene(root);
            addStyles(scene);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException exception) {
            throw new IllegalStateException("Impossible de charger la vue : " + resourcePath, exception);
        }
    }

    private void addStyles(Scene scene) {
        scene.getStylesheets().setAll(
                resourceToExternal("/com/artkids/css/theme.css"),
                resourceToExternal("/com/artkids/css/components.css"),
                resourceToExternal("/com/artkids/css/forms.css"),
                resourceToExternal("/com/artkids/css/tables.css"),
                resourceToExternal("/com/artkids/css/frontoffice.css"),
                resourceToExternal("/com/artkids/css/backoffice.css")
        );
    }

    private String resourceToExternal(String path) {
        URL resource = SceneManager.class.getResource(path);
        if (resource == null) {
            throw new IllegalStateException("Ressource CSS introuvable : " + path);
        }
        return resource.toExternalForm();
    }
}
